package com.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.commons.security.DefaultSecurityConfig;
import com.commons.security.FeignTokenRelayConfig;

@SpringBootApplication(scanBasePackages = {"com.settlement"})
@EnableFeignClients(basePackages = "com.settlement.client") 
@Import({DefaultSecurityConfig.class, FeignTokenRelayConfig.class})
@EnableScheduling
public class SettlementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SettlementServiceApplication.class, args);
	}

}
