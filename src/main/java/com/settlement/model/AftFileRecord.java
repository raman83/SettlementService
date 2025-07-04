package com.settlement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AftFileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String batchDate;
    private int recordCount;

    @Enumerated(EnumType.STRING)
    private SettlementStatus status;

    private String failureReason;
    private Instant ackReceivedAt;
}
