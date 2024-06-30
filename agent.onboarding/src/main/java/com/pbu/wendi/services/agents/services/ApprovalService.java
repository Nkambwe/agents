package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.utils.requests.agents.dto.ApprovalRequest;
import com.pbu.wendi.utils.enums.AgentType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ApprovalService {
    CompletableFuture<Boolean> existsById(long id);
    CompletableFuture<Boolean>  existsByPersonId(long personId);
    CompletableFuture<Boolean>  existsByBusinessId(long businessId);
    CompletableFuture<ApprovalRequest> findApprovalById(long id);
    CompletableFuture<List<ApprovalRequest>> getApprovals(long agentId, AgentType type);
    CompletableFuture<ApprovalRequest> create(ApprovalRequest approval, AgentType type) throws InterruptedException;
    void update(ApprovalRequest approval);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}

