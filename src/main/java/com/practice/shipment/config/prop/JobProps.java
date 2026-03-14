package com.practice.shipment.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "scheduled-job")
@Data
public class JobProps {

    private final Prop paymentValidation = new Prop();
    @Data
    public static class Prop{
        private int pollingInterval;
        private int attempts;
    }
}
