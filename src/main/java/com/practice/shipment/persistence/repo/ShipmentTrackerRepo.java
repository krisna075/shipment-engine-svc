package com.practice.shipment.persistence.repo;

import com.practice.shipment.dto.Enum;
import com.practice.shipment.persistence.entity.ShipmentTracker;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentTrackerRepo extends JpaRepository<ShipmentTracker, ShipmentTracker.ShipmentTrackerPk> {

    List<ShipmentTracker> findByOrderId(String orderId);
    List<ShipmentTracker> findByStageAndStatus(Enum.Stage stage, Enum.Status status, Pageable pageable);
}
