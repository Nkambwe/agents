package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.utils.requests.sam.dto.SetRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SetService {
    boolean nameInUse(String name);
    boolean checkNameDuplication(String name, long id);
    boolean descriptionInUse(String description);
    boolean checkDescriptionDuplication(String description, long id);
    CompletableFuture<List<SetRequest>> findAll();
    CompletableFuture<SetRequest> findById(long id);
    CompletableFuture<SetRequest> findBySetName(String name);
    CompletableFuture<SetRequest> create(SetRequest permission);
    void lockSet(long id, boolean isLocked);
    void softDelete(long id, boolean deleted);
    void delete(long id);
    CompletableFuture<SetRequest> update(SetRequest permission);
    CompletableFuture<SetRequest> updateSetPermissions(SetRequest permission);
}
