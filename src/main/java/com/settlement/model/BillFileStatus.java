package com.settlement.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BillFileStatus {
    @Id
    private String fileName;
    private String settlementDate;
    private Instant createdAt;
    private int recordCount;
    private String status;
}