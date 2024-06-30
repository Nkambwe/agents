package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.utils.requests.agents.dto.SignatoryRequest;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SignatoryService {
    boolean exists(long id);
    boolean nameInUse(String name);
    boolean duplicatedName(String name, long id);
    boolean duplicatedSignatoryName(String name, long agentId);
    CompletableFuture<SignatoryRequest> findById(Long id);
    CompletableFuture<List<SignatoryRequest>> findByBusinessId(Long businessId);
    CompletableFuture<List<SignatoryRequest>> findByPersonId(Long personId);

    CompletableFuture<SignatoryRequest> createSignatory(SignatoryRequest signatory);
    void updateSignatory(SignatoryRequest signatory);
    void softDelete(@Param("id") long id, @Param("status") boolean status);
    void delete(long id);
}

