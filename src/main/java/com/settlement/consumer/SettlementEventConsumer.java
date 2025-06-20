package com.settlement.consumer;

import com.settlement.dto.AftSettlementTriggerEvent;
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
        processor.processFile(event.getFileName());
    }
}