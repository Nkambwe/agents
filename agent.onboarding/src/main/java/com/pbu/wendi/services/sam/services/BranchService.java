package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.utils.requests.sam.dto.BranchRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BranchService {
    /**
     * Find branch by its name
     * @param id branch ID to find
     * @return System branch object if found else returns null
     * **/
    CompletableFuture<BranchRequest> findById(long id);

    /**
     * find branch by its Solid
     * @param solId branch solId to find
     * @return System branch object if found else returns null
     **/
    CompletableFuture<BranchRequest> findBySolId(String solId);

    /**
     * find branch by its Solid
     * @param name branch name to find
     * @return System branch object if found else returns null
     **/
    CompletableFuture<BranchRequest> findByName(String name);
    /**
     * Get all system branches.
     * @return list of system branches else returns an empty list
     */
    CompletableFuture<List<BranchRequest>> getAll();
    /**
     * check if branch exists with given id
     * @param id branch ID to find
     * @return true if branch exists else false
     **/
    boolean checkIfExistsById(long id);
    /**
     * check if branch exists with given solid
     * @param solId branch solId to find
     * @return true if branch exists else false
     **/
    boolean checkIfExistsBySolId(String solId);

    /**check whether branch with solID {@code solId} exists other than one with ID
     * @param branchId branch ID which owns name
     * @param solId branch solId to find
     * @return true if branch exists else false
     **/
    boolean checkSolIdDuplication(String solId,long branchId);

    /**check whether branch exists with name {@code branchName}
     * @param branchName branch name to find
     * @return true if branch exists else false
     **/
    boolean checkIfExistsByName(String branchName);

    /**check whether branch with name {@code branchName} exists other than one with ID
     * @param branchId branch ID which owns name
     * @param branchName branch name to find
     * @return true if branch exists else false
     **/
    boolean checkNameDuplication(String branchName,long branchId);

    /**Update branch object with {@code id} to active status {@code status}
     * @param id branch ID to update
     * @param status branch status to set
     **/
    void activateBranch(long id, boolean status);
    /**
     * Create new branch object {@code branch}
     * @param branch User object to create
     * @return User record created
     **/
    CompletableFuture<BranchRequest> create(BranchRequest branch) throws InterruptedException;

    /**Update branch record
     * @param branch branch to update
     **/
    void updateBranch(BranchRequest branch);
    /**
     * Soft delete branch with specified ID
     * @param id branch ID to find
     * **/
    void deleteBranch(long id);
    /**
     * Permanently delete branch with specified ID
     * @param id branch ID to find
     * **/

    void softDelete(long id, boolean isDeleted);

}

