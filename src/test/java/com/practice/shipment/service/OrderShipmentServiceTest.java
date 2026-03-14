package com.practice.shipment.service;

import com.practice.shipment.dto.OrderRequestDto;
import com.practice.shipment.persistence.entity.ShipmentTracker;
import com.practice.shipment.persistence.repo.ShipmentTrackerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderShipmentServiceTest {
    @Mock
    private ShipmentTrackerRepo shipmentTrackerRepo;
    @InjectMocks
    private OrderShipmentService orderShipmentService;

    private OrderRequestDto getOrderRequestDto(){
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId("test1");
        orderRequestDto.setPaymentId("testpay1");

        OrderRequestDto.Product product = new OrderRequestDto.Product();
        product.setProductId("testId1");
        product.setProductName("testName1");
        orderRequestDto.setProducts(List.of(product));
        return orderRequestDto;
    }

    @Test
    public void testCreateOrderShipmentForNewOrder(){
        when(this.shipmentTrackerRepo.existsById(Mockito.any())).thenReturn(false);
        List<ShipmentTracker> orderShipments = this.orderShipmentService.createOrderShipment(this.getOrderRequestDto());
        verify(this.shipmentTrackerRepo, atLeastOnce()).saveAll(Mockito.any());
    }
}
