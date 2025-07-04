// --- PaymentClient.java (Feign) ---
package com.settlement.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-service", url = "http://localhost:8088")
public interface PaymentClient {
	@PutMapping("/payments/{paymentId}/status")
	public ResponseEntity<Void> updatePaymentStatus(
	    @PathVariable("paymentId") UUID paymentId,
	    @RequestParam("status") String status,
	    @RequestParam(name = "reason",  required = false) String reason
	);
}
