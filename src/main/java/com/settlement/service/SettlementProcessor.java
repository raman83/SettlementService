package com.settlement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.batch.dto.AftSettlementTriggerEvent;
import com.rtr.dto.Pacs002Response;
import com.settlement.client.PaymentClient;
import com.settlement.dto.Camt002AckEvent;
import com.settlement.model.AftFileRecord;
import com.settlement.model.BillFileStatus;
import com.settlement.model.SettlementStatus;
import com.settlement.repository.AftFileRecordRepository;
import com.settlement.repository.BillFileStatusRepository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementProcessor {

	  private final AftFileRecordRepository repository;
	  private final BillFileStatusRepository billFileStatusRepository;

	  private final PaymentClient paymentClient;
    public void processFile(AftSettlementTriggerEvent event) {
    	 Path path = Path.of("/Users/reyansh/Desktop/raman/Auth/batch", event.getFileName());

         if (!Files.exists(path)) {
             log.error("❌ File not found: {}", path);
             repository.save(AftFileRecord.builder()
                     .fileName(event.getFileName())
                     .batchDate(event.getBatchDate())
                     .recordCount(event.getRecordCount())
                     .status(SettlementStatus.FAILED)
                     .failureReason("File not found")
                     .build());
             return;
         }

         // Save with status SENT
         repository.save(AftFileRecord.builder()
                 .fileName(event.getFileName())
                 .batchDate(event.getBatchDate())
                 .recordCount(event.getRecordCount())
                 .status(SettlementStatus.SENT)
                 .build());

         log.info("✅ File {} sent to TD", event.getFileName());
    }
    
    public void processBillFile(String fileName, String date, int totalRecords) {
        BillFileStatus status = new BillFileStatus();
        status.setFileName(fileName);
        status.setCreatedAt(Instant.now());
        status.setSettlementDate(date);
        status.setRecordCount(totalRecords);
        status.setStatus("SENT");

        billFileStatusRepository.save(status);
    }
    
    
    public void markFileAsProcessedAndSettlePayments(Camt002AckEvent ack) {
    	 // 1. Update file status in local settlement DB
        AftFileRecord fileEntity = repository.findByFileName(ack.getFileName())
            .orElseThrow(() -> new RuntimeException("AFT file not found"));
        fileEntity.setStatus(SettlementStatus.valueOf("PROCESSED"));
        fileEntity.setAckReceivedAt(ack.getAckReceivedAt());
        repository.save(fileEntity);
        
        
       

        // 2. Forward payment updates to PaymentService via Feign client
        for (Camt002AckEvent.PaymentAckStatus paymentStatus : ack.getRecords()) {
            try {
                updateCanonicalPayment(paymentStatus);
            } catch (Exception ex) {
                log.error("⚠️ Failed to update payment status for {}", paymentStatus.getPaymentId(), ex);
            }
        }
}
    
    
    public void processRtrPayment(Pacs002Response pacs002) {
    	 if (!"ACCP".equalsIgnoreCase(pacs002.getTransactionStatus())) {
    	        log.warn("⏭ Payment {} was not accepted. Status: {}, Reason: {}",
    	                 pacs002.getOriginalPaymentId(),
    	                 pacs002.getTransactionStatus(),
    	                 pacs002.getReasonCode());
    	        return;
    	    }

    	    log.info("💸 Settling RTR payment: {} -> {} : ${}",
    	             pacs002.getFromAccount(),
    	             pacs002.getToAccount(),
    	             pacs002.getAmount());

    	    // Simulate debit & credit here
    	    // e.g., accountService.debit(pacs002.getFromAccount(), pacs002.getAmount());

    	    log.info("✅ RTR payment {} settled successfully at {}", 
    	             pacs002.getOriginalPaymentId(), 
    	             pacs002.getSettlementTimestamp());
    }
    
    
    private void updateCanonicalPayment(Camt002AckEvent.PaymentAckStatus status) {
        paymentClient.updatePaymentStatus(
            status.getPaymentId(),
            status.getStatus(),
            status.getReason()
        );
    }

    
    
}