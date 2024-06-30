package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.AffiliationRequest;
import com.pbu.wendi.requests.agents.dto.AgentRequest;
import com.pbu.wendi.requests.agents.dto.BusinessRequest;
import com.pbu.wendi.requests.agents.dto.IndividualRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AgentService {

    //region Individual Agents
    CompletableFuture<Boolean> duplicateIndividualSortCode(String sortCode);
    CompletableFuture<Boolean> duplicateIndividualPinNumber(String pin);
    CompletableFuture<Boolean> duplicatePersonalTin(String tin);
    CompletableFuture<Boolean> individualExists(long id);
    CompletableFuture<IndividualRequest> findIndividual(long id);
    CompletableFuture<IndividualRequest> findIndividualSortCode(String sortCode);
    CompletableFuture<List<IndividualRequest>> getIndividuals();
    CompletableFuture<List<IndividualRequest>> getIndividuals(boolean isDeleted);
    CompletableFuture<IndividualRequest> createIndividual(IndividualRequest person, AffiliationRequest org) throws InterruptedException;
    void createIndividualAgents(List<IndividualRequest> records, long userId);
    void updateIndividual(IndividualRequest person, AffiliationRequest org);
    void softDeleteIndividual(long id, boolean deleted);
    void deleteIndividual(long id);

    //endregion

    //region Business Agents
    CompletableFuture<Boolean> businessExists(long id);
    CompletableFuture<Boolean> duplicateSortCode(String sortCode);
    CompletableFuture<Boolean> duplicateBusinessPinNumber(String pin);
    CompletableFuture<Boolean> duplicateBusinessTin(String tin);
    CompletableFuture<Boolean> duplicateBusinessNameWithDifferentIds(String name, long id);
    CompletableFuture<BusinessRequest>  findBusinessById(long id);
    CompletableFuture<BusinessRequest> findBusinessSortCode(String sortCode);
    CompletableFuture<BusinessRequest>  findBusinessByName(String name);
    CompletableFuture<List<BusinessRequest>> getBusinesses();
    CompletableFuture<List<BusinessRequest>> getBusinesses(boolean isDeleted);
    CompletableFuture<BusinessRequest> createBusiness(BusinessRequest business, AffiliationRequest org) throws InterruptedException;
    void createBusinessAgents(List<BusinessRequest> records, long userId);
    void updateBusiness(BusinessRequest business, AffiliationRequest org);
    void softDeleteBusiness(long id, boolean deleted);
    void deleteBusiness(long id);
    //endregion

    //region general
    CompletableFuture<Boolean> isDuplicateCode(String sortCode);
    CompletableFuture<Boolean> isDuplicatePin(String pin);
    CompletableFuture<Boolean> isDuplicateTin(String tin);
    CompletableFuture<List<AgentRequest>> getAgents();
    CompletableFuture<List<AgentRequest>> getAgents(boolean isDeleted);
    CompletableFuture<List<AgentRequest>> getAgents(int type);
    CompletableFuture<List<AgentRequest>> getAgents(int type, boolean isDeleted);
    CompletableFuture<List<AgentRequest>> getAffiliationAgents(long affiliationId);


    //endregion

}
