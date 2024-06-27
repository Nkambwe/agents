package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.ArchiveLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArchiveLogRepository extends JpaRepository<ArchiveLog, Long> {
    List<ArchiveLog> findAllByArchivedOn(LocalDateTime logDate);
}
