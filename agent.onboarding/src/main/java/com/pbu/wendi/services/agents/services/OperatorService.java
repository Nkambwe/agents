package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.BusinessRequest;
import com.pbu.wendi.requests.agents.dto.IndividualRequest;
import com.pbu.wendi.requests.agents.dto.OperatorRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OperatorService {
    boolean exists(long id);

    boolean duplicatedName(String name, long agentId, long id);
    boolean duplicatedNIN(String nin);
    boolean duplicatedNIN(String nin, long recordId);

    CompletableFuture<Boolean> findByOperatorNameAndBusinessId(String operatorName, Long agentId);
    CompletableFuture<Boolean> findByOperatorNameAndPersonId(String operatorName, Long agentId);
    CompletableFuture<List<OperatorRequest>> findIndividualOperators(Long personId);
    CompletableFuture<List<OperatorRequest>> findBusinessOperators(Long businessId);
    CompletableFuture<OperatorRequest> create(OperatorRequest operator, IndividualRequest person, BusinessRequest business) throws InterruptedException;
    void update(OperatorRequest operator, IndividualRequest person, BusinessRequest business);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}

