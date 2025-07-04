package com.settlement.consumer;

import com.batch.dto.AftSettlementTriggerEvent;
import com.batch.dto.BillSettlementTriggerEvent;
import com.common.iso.CanonicalPayment;
import com.rtr.dto.Pacs002Response;
import com.settlement.dto.Camt002AckEvent;
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
    public void handleAftFileTrigger(AftSettlementTriggerEvent event) {
        log.info("📥 Received AFT settlement trigger for file: {}", event.getFileName());
        processor.processFile(event);
    }

    @KafkaListener(topics = "rtr.payment.status", groupId = "settlement-group")
    public void handleRtrPacs002(Pacs002Response pacs002) {
        log.info("🔔 Received pacs.002 response: {}", pacs002);
        processor.processRtrPayment(pacs002); // Optional for RTR — still useful if you're listening here
    }

    @KafkaListener(topics = "camt.002.ack", groupId = "settlement-group")
    public void handleCamt002Ack(Camt002AckEvent ack) {
        log.info("📨 CAMT.002 ack received: {}", ack.getFileName());
        processor.markFileAsProcessedAndSettlePayments(ack);
    }
    
    @KafkaListener(
            topics = "rtr.payment.status",
            groupId = "settlement-group"        )
        public void listen(Pacs002Response pacs002) {
            log.info("🔔 Received pacs.002: {}", pacs002);
            processor.processRtrPayment(pacs002);
        }
    
    
    @KafkaListener(topics = "bill.payment.ready-for-settlement", groupId = "settlement-group")
    public void handleBillPaymentReady(BillSettlementTriggerEvent event) {
        log.info("🔔 Received bill.payment.ready-for-settlement event: {}", event);
        processor.processBillFile(event.getFileName(), event.getFileDate(), event.getRecordCount());
    }
}