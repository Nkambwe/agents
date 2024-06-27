package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.BusinessNatureRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BusinessNatureService {
    CompletableFuture<Boolean> businessNatureExists(long id);
    CompletableFuture<Boolean> existsByNature(String nature);
    CompletableFuture<Boolean>  existsByNatureAndNotId(String nature, Long id);
    CompletableFuture<BusinessNatureRequest> findBusinessNatureById(long id);
    CompletableFuture<BusinessNatureRequest> findBusinessNatureByNature(String nature);
    CompletableFuture<List<BusinessNatureRequest>> getAllBusinessNatures();
    CompletableFuture<List<BusinessNatureRequest>> getActiveBusinessNatures();
    CompletableFuture<BusinessNatureRequest> create(BusinessNatureRequest biz_nature) throws InterruptedException;
    void update(BusinessNatureRequest biz_nature);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
