package com.example.aidebuggerservice.repository;

import com.example.aidebuggerservice.model.IncidentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRecordRepository
        extends JpaRepository<IncidentRecord, Long> {
}