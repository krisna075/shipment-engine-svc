package com.practice.shipment.service;

import com.practice.shipment.config.prop.UrlProps;
import com.practice.shipment.dto.PaymentValidationResponseDto;
import com.practice.shipment.exception.ShipmentEngineException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentValidationService {

    private final RestTemplate restTemplate;
    private final UrlProps urlProps;

    public PaymentValidationResponseDto invokePaymentValidationService(String paymentId) throws ShipmentEngineException {
        String url = this.urlProps.getPaymentValidation().getPath();

        String finalUrl = UriComponentsBuilder.fromPath(url)
                .queryParam("paymentId", paymentId)
                .build()
                .toUriString();

        ResponseEntity<PaymentValidationResponseDto> response = this.restTemplate.postForEntity(finalUrl, null, PaymentValidationResponseDto.class);


        log.info("Response - {}", response);

        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else{
            throw new ShipmentEngineException("Received unsuccessful response from payment validation API");
        }
    }
}
