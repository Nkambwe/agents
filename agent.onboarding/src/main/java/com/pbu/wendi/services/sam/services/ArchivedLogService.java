package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.requests.sam.dto.ArchiveLogRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArchivedLogService {
    CompletableFuture<List<ArchiveLogRequest>> findAll();
    CompletableFuture<List<ArchiveLogRequest>>  findAll(LocalDateTime logDate);
    CompletableFuture<ArchiveLogRequest> create(ArchiveLogRequest log) throws InterruptedException;
    void delete(long id);
}

