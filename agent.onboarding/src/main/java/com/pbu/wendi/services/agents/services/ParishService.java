package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.utils.requests.agents.dto.ParishRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ParishService {
    CompletableFuture<Boolean> parishExists(long id);
    CompletableFuture<Boolean> existsByName(String parishName);
    CompletableFuture<Boolean>  existsByNameAndNotId(String parishName, Long id);
    CompletableFuture<ParishRequest> findParishById(long id);
    CompletableFuture<ParishRequest> findParishByName(String parishName);
    CompletableFuture<List<ParishRequest>> getAllParishes();
    CompletableFuture<List<ParishRequest>> getActiveParishes();
    CompletableFuture<ParishRequest> create(ParishRequest parish) throws InterruptedException;
    void update(ParishRequest parish);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
