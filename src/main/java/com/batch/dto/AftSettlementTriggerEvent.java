package com.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AftSettlementTriggerEvent {
    private String fileName;
    private String batchDate;
    private int recordCount;
    private String eventType = "payment.ready-for-settlement";
}