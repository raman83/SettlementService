package com.batch.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillSettlementTriggerEvent {
    private String fileName;
    private String fileDate;
    private int recordCount;
    private String eventType = "billpayment.ready-for-settlement";
}
