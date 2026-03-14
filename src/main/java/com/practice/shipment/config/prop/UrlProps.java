package com.practice.shipment.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api-url")
@Data
public class UrlProps {

    private final Prop paymentValidation = new Prop();

    @Data
    public static class Prop{
        String path;
    }
}
