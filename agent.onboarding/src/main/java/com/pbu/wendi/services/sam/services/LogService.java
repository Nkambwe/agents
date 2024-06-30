package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.requests.sam.dto.LogRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LogService {
    CompletableFuture<List<LogRequest>> findAll();
    CompletableFuture<LogRequest> create(LogRequest log);
    CompletableFuture<List<LogRequest>> findAll(LocalDateTime startDate, LocalDateTime endDate);
}
