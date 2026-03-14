package com.practice.shipment.service;

import com.practice.shipment.dto.Enum;
import com.practice.shipment.dto.OrderRequestDto;
import com.practice.shipment.persistence.entity.ShipmentTracker;
import com.practice.shipment.persistence.repo.ShipmentTrackerRepo;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderShipmentService {
    private final ShipmentTrackerRepo shipmentTrackerRepo;

    public List<ShipmentTracker> createOrderShipment(OrderRequestDto orderRequestDto){
        String orderId = orderRequestDto.getOrderId();
        String paymentId = orderRequestDto.getPaymentId();
        List<ShipmentTracker> preparedShipmentTrackers = orderRequestDto.getProducts().stream()
                .filter(e -> StringUtils.isNotEmpty(e.getProductId()))
                .filter(e -> !this.recordExistsForProduct(orderId, e))
                .map(e -> {
                    ShipmentTracker shipmentTracker = new ShipmentTracker();
                    shipmentTracker.setOrderId(orderId);
                    shipmentTracker.setProductId(e.getProductId());
                    shipmentTracker.setProductName(e.getProductName());
                    shipmentTracker.setStage(Enum.Stage.PAYMENT_VALIDATION);
                    shipmentTracker.setStatus(Enum.Status.DRAFT);
                    shipmentTracker.setPaymentId(paymentId);
                    return shipmentTracker;
                })
                .toList();

        this.shipmentTrackerRepo.saveAll(preparedShipmentTrackers);
        return preparedShipmentTrackers;
    }

    private boolean recordExistsForProduct(String orderId, OrderRequestDto.Product product) {
        boolean recordExists = this.shipmentTrackerRepo.existsById(new ShipmentTracker.ShipmentTrackerPk(orderId, product.getProductId()));
        if(recordExists){
            log.info("Skipped. Record already exists for product Id - {}", product);
            return true;
        }else {
            return false;
        }
    }
}
