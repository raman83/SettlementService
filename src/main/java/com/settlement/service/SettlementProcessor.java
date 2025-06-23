package com.settlement.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.common.iso.CanonicalPayment;
import com.rtr.dto.Pacs002Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
public class SettlementProcessor {

    public void processFile(String fileName) {
        Path filePath = Path.of("/Users/reyansh/Desktop/raman/Auth/batch", fileName);

        if (!Files.exists(filePath)) {
            log.error("❌ File not found: {}", filePath);
            return;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (line.startsWith("200")) {
                    String fromAcc = line.substring(2, 17).trim();
                    String toAcc = line.substring(17, 32).trim();
                    String amountStr = line.substring(41, 51).trim();
                    BigDecimal amount = new BigDecimal(amountStr).movePointLeft(2);

                    log.info("💸 Settling: {} -> {} : ${}", fromAcc, toAcc, amount);
                    // Simulate debit & credit logic here
                }
            }
            log.info("✅ Settlement complete for file: {}", fileName);
        } catch (IOException e) {
            log.error("⚠️ Error reading AFT file", e);
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
}