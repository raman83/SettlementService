package com.settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.settlement.model.BillFileStatus;

public interface BillFileStatusRepository extends JpaRepository<BillFileStatus, String> {
}