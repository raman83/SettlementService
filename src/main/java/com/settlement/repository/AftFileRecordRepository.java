// --- AftFileRecordRepository.java ---
package com.settlement.repository;

import com.settlement.model.AftFileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AftFileRecordRepository extends JpaRepository<AftFileRecord, Long> {
    Optional<AftFileRecord> findByFileName(String fileName);
}