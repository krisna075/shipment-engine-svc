package com.practice.shipment.job;

import com.practice.shipment.config.prop.JobProps;
import com.practice.shipment.service.OrderRequestProcessJobService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Jobs {
    private final OrderRequestProcessJobService orderRequestProcessJobService;
    private final JobProps jobProps;

    @Scheduled(fixedRateString = "#{@jobProps.getPaymentValidation().getPollingInterval()}", initialDelay = 1000)
    public void runRequestProcessJob(){
        this.orderRequestProcessJobService.processReceivedOrderRequest();
    }

    @PostConstruct
    public void init(){
        log.info("Payment Validation job started with delay of {}", this.jobProps.getPaymentValidation().getPollingInterval());
    }
}
