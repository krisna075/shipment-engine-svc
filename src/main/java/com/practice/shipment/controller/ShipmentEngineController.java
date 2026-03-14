package com.practice.shipment.controller;

import com.practice.shipment.dto.OrderRequestDto;
import com.practice.shipment.persistence.entity.ShipmentTracker;
import com.practice.shipment.service.OrderShipmentService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipment-engine-svc")
@Slf4j
@RequiredArgsConstructor
public class ShipmentEngineController {

    private final OrderShipmentService orderShipmentService;

    @PostMapping(path = "/createOrderShipment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrderShipment(@RequestBody OrderRequestDto orderRequestDto){
        if(orderRequestDto == null || StringUtils.isEmpty(orderRequestDto.getOrderId()) || CollectionUtils.isEmpty(orderRequestDto.getProducts())){
            return ResponseEntity.badRequest()
                    .body("Invalid Request. Order Id and at least one product needs to be present.");
        }
        try{
            List<ShipmentTracker> shipmentRecords = this.orderShipmentService.createOrderShipment(orderRequestDto);
            return ResponseEntity.ok(shipmentRecords);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
