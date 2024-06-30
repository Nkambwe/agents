package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.utils.requests.agents.dto.BankRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BankService {
    /**
     * Check if bank with specified id exists
     * @param id Type ID to look for
     **/
    CompletableFuture<Boolean> bankExists(long id);

    /**
     * Check if bank with specified name exists
     * @param name bank name to look for
     **/
    boolean checkBankDuplicateName(String name);
    /**
     * Check whether a bank with duplicate name with ID other than that provided
     * @param name bank name to look for
     * @param id bank ID to compare to
     **/
    boolean checkBankDuplicateNameWithDifferentIds(String name, long id);
    /**
     * Check if bank with specified name exists
     * @param sort_code bank sort code to look for
     **/
    boolean checkBankDuplicateSortCode(String sort_code);
    /**
     * Check whether a bank with duplicate name with ID other than that provided
     * @param sort_code bank sort code to look for
     * @param id bank ID to compare to
     **/
    boolean checkBankDuplicateSortCodeWithDifferentIds(String sort_code, long id);
    /**
     * Find bank with specified ID
     * @param id object ID to look for
     **/
    CompletableFuture<BankRequest> findBankById(long id);
    /**
     * Find bank with specified ID
     * @param sort_code object sort code to look for
     **/
    CompletableFuture<BankRequest> findBankBySortCode(String sort_code);
    /**
     * Get all banks
     **/
    CompletableFuture<List<BankRequest>> getBanks();

    /**
     * Get all active banks
     **/
    CompletableFuture<List<BankRequest>> getActiveBanks();

    /**
     * Add a new bank
     * @param bank object to add
     **/
    CompletableFuture<BankRequest> createBank(BankRequest bank) throws InterruptedException;

    /**
     * Update bank
     * @param bank object to update
     **/
    void updateBank(BankRequest bank);

    /**
     * Soft delete bank with specified ID
     * @param id for bank to delete
     * @param is_deleted delete status
     * **/
    void softDeleteBank(long id, boolean is_deleted);

    /**
     * Soft delete bank with specified ID
     * @param id for bank to delete
     * **/
    void deleteBank(long id);
}
