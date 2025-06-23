package com.settlement.consumer;

import com.ach.dto.AftSettlementTriggerEvent;
import com.common.iso.CanonicalPayment;
import com.rtr.dto.Pacs002Response;
import com.settlement.service.SettlementProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementEventConsumer {

    private final SettlementProcessor processor;

    @KafkaListener(topics = "${kafka.topic.settlement}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(AftSettlementTriggerEvent event) {
        log.info("📥 Received settlement trigger for file: {}", event.getFileName());
        log.info("Received settlement trigger for file:", event.getFileName());
        processor.processFile(event.getFileName());
    }
    
    
    @KafkaListener(
            topics = "rtr.payment.status",
            groupId = "settlement-group"        )
        public void listen(Pacs002Response pacs002) {
            log.info("🔔 Received pacs.002: {}", pacs002);
            processor.processRtrPayment(pacs002);
        }
}