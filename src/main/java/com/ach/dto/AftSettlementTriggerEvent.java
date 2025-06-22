package com.ach.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AftSettlementTriggerEvent {
    private String fileName;
    private LocalDate batchDate;
    private int recordCount;
    private String eventType;
}
