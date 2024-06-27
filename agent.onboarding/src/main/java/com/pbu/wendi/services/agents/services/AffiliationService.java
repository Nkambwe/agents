package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.AffiliationRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AffiliationService {

    /**
     * Check if affiliated organization with specified id exists
     * @param id Type ID to look for
     **/
    CompletableFuture<Boolean> affiliationExists(long id);

    /**
     * Check if affiliated organization with specified name exists
     * @param name affiliation name to look for
     **/
    boolean checkAffiliationDuplicateName(String name);
    /**
     * Check whether an affiliation with duplicate name with ID other than that provided
     * @param name affiliation name to look for
     * @param id affiliation ID to compare to
     **/
    boolean checkAffiliationDuplicateNameWithDifferentIds(String name, long id);
    /**
     * Check if affiliated organization with specified name exists
     * @param sort_code affiliation sort code to look for
     **/
    boolean checkAffiliationDuplicateSortCode(String sort_code);
    /**
     * Check whether an affiliation with duplicate name with ID other than that provided
     * @param sort_code affiliation sort code to look for
     * @param id affiliation ID to compare to
     **/
    boolean checkAffiliationDuplicateSortCodeWithDifferentIds(String sort_code, long id);
    /**
     * Find affiliated organization with specified ID
     * @param id object ID to look for
     **/
    CompletableFuture<AffiliationRequest> findAffiliationById(long id);
    /**
     * Find affiliated organization with specified ID
     * @param sort_code object sort code to look for
     **/
    CompletableFuture<AffiliationRequest> findAffiliationBySortCode(String sort_code);
    /**
     * Get all affiliated organizations types
     **/
    CompletableFuture<List<AffiliationRequest>> getAffiliations();

    /**
     * Get all affiliated organizations types
     **/
    CompletableFuture<List<AffiliationRequest>> getActiveAffiliations();

    /**
     * Add a new affiliation organization
     * @param affiliation object to add
     **/
    CompletableFuture<AffiliationRequest> createAffiliation(AffiliationRequest affiliation) throws InterruptedException;

    /**
     * Update affiliation organization
     * @param affiliation object to update
     **/
    void updateAffiliation(AffiliationRequest affiliation);

    /**
     * Soft delete affiliation with specified ID
     * @param id for affiliation organization to delete
     * @param is_deleted delete status
     * **/
    void softDeleteAffiliation(long id, boolean is_deleted);

    /**
     * Soft delete affiliation organization with specified ID
     * @param id for affiliation to delete
     * **/
    void deleteAffiliation(long id);

}
