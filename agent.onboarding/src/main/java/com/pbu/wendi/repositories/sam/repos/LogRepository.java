package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends JpaRepository<SystemLog, Long> {
    @Query(value = "SELECT sl FROM SystemLog sl ORDER BY sl.id DESC")
    List<SystemLog> findTop300();

    @Query(value = "SELECT sl FROM SystemLog sl WHERE sl.logTime BETWEEN :startDate AND :endDate ORDER BY sl.logTime DESC")
    List<SystemLog> findTop300(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
