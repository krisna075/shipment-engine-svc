package com.practice.shipment.persistence.entity;

import com.practice.shipment.dto.Enum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "SHIPMENT_TRACKER")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(ShipmentTracker.ShipmentTrackerPk.class)
public class ShipmentTracker {
    @Id
    @Column(name = "ORDER_ID")
    private String orderId;

    @Id
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PAYMENT_ID")
    private String paymentId;

    @Column(name = "SHIPPING_ID")
    private String shippingId;

    @Column(name = "SHIPPING_STATUS")
    private String shippingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "STAGE")
    private Enum.Stage stage;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Enum.Status status;

    @Column(name = "ATTEMPTS")
    private int attempts;

    @Lob
    @Column(name = "PROCESS_MSG")
    private String processMsg;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor(force = true)
    public static class ShipmentTrackerPk {
        private String orderId;
        private String productId;
    }
}
