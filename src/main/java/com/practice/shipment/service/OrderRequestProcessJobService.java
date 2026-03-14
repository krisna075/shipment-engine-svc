package com.practice.shipment.service;

import com.practice.shipment.config.prop.JobProps;
import com.practice.shipment.dto.Enum;
import com.practice.shipment.dto.PaymentValidationResponseDto;
import com.practice.shipment.persistence.entity.ShipmentTracker;
import com.practice.shipment.persistence.repo.ShipmentTrackerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderRequestProcessJobService {

    private final ShipmentTrackerRepo shipmentTrackerRepo;

    private final PaymentValidationService paymentValidationService;

    private final JobProps jobProps;

    public void processReceivedOrderRequest() {
        try {
            List<ShipmentTracker> pendingShipments = this.shipmentTrackerRepo.findByStageAndStatus(Enum.Stage.PAYMENT_VALIDATION,
                    Enum.Status.DRAFT, Pageable.ofSize(5));

            if (CollectionUtils.isEmpty(pendingShipments)) {
                log.info("No records found to process");
                return;
            }

            log.info("Processing Shipment tracker count: {}", pendingShipments.size());

            List<Boolean> processResults = pendingShipments.stream()
                    .map(this::validatePayment)
                    .toList();

            long success = processResults.stream()
                    .filter(e -> e)
                    .count();

            log.info("Processed with success count: {}, failed count: {}", success, (processResults.size() - success));

        } catch (Exception ex) {
            log.error("Exception occurred on scheduled payment validation job - {}", ex.getMessage(), ex);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean validatePayment(ShipmentTracker shipmentTracker) {
        boolean processResult = false;
        shipmentTracker.setAttempts(shipmentTracker.getAttempts() + 1);
        try {
            String paymentId = shipmentTracker.getPaymentId();

            PaymentValidationResponseDto paymentValidationResponseDto = this.paymentValidationService.invokePaymentValidationService(paymentId);
            if (paymentValidationResponseDto.getResult().equalsIgnoreCase("SUCCESS")) {
                shipmentTracker.setStatus(Enum.Status.COMPLETED);
                processResult = true;
            } else {
                shipmentTracker.setStatus(Enum.Status.FAILED);
                shipmentTracker.setProcessMsg(paymentValidationResponseDto.getResultMsg());
            }
        } catch (Exception e) {
            log.error("Exception occurred during payment validation for record - {} - {}", shipmentTracker, e.getMessage(), e);
            int maxAttempts = this.jobProps.getPaymentValidation().getAttempts();
            //if exceeds max attempts, mark as error
            if (shipmentTracker.getAttempts() >= maxAttempts) {
                shipmentTracker.setStatus(Enum.Status.ERROR);
                shipmentTracker.setProcessMsg(StringUtils.truncate(e.getMessage(), 1000));
            }
        } finally {
            this.shipmentTrackerRepo.save(shipmentTracker);
        }
        return processResult;
    }
}
