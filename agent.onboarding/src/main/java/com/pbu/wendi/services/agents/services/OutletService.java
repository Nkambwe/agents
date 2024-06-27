package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.OutletRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OutletService {
    CompletableFuture<Boolean> outletExists(long id);
    CompletableFuture<Boolean> existsByName(String name);
    CompletableFuture<Boolean>  existsByNameAndNotId(String name, Long id);
    CompletableFuture<OutletRequest> findOutletsById(long id);
    CompletableFuture<OutletRequest>  findOutletsByName(String name);
    CompletableFuture<List<OutletRequest>> getAllOutlets();
    CompletableFuture<List<OutletRequest>> getActiveOutlets();
    CompletableFuture<OutletRequest> create(OutletRequest outlet) throws InterruptedException;
    void update(OutletRequest outlet);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
