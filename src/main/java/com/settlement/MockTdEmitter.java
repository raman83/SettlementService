package com.settlement;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.settlement.dto.Camt002AckEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MockTdEmitter {
	@Autowired
	ObjectMapper objectMapper ;
    private final KafkaTemplate<String, Object> kafkaTemplate;

  // @Scheduled(fixedDelay = 30000) // Every 30 seconds, for example
    public void emitCamt002() {
    	Camt002AckEvent ack = new Camt002AckEvent(
    	        "AFT_0ab724c6-572e-4897-b9b0-56a46cd5c6ee.txt",
    	        "PARTIALLY_ACCEPTED",
    	        Instant.now(),
    	        List.of(
    	            new Camt002AckEvent.PaymentAckStatus(
    	                UUID.fromString("d5d2f6b5-c5f3-4101-bef3-0c344f668dc4"), "ACCEPTED", null
    	            ),
    	            new Camt002AckEvent.PaymentAckStatus(
    	                UUID.fromString("d3741722-cd1c-4895-a28e-e5db32ed53aa"), "REJECTED", "AC01"
    	            )
    	        )
    	    );

    	kafkaTemplate.send("camt.002.ack", ack);

    }
}
