package com.practice.shipment.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDto {
    private String orderId;
    private List<Product> products = new ArrayList<>();
    private String paymentId;

    @Data
    public static class Product {
        private String productId;
        private String productName;
    }
}
