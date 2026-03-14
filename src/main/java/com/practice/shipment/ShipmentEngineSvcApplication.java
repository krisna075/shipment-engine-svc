package com.practice.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.practice.shipment")
@EnableScheduling
@EnableJpaRepositories
public class ShipmentEngineSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipmentEngineSvcApplication.class, args);
	}
}
