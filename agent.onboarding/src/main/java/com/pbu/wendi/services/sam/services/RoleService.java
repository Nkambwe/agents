package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.requests.sam.dto.RoleRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RoleService {
    boolean exists(long roleId);
    boolean nameInUse(String name);
    boolean checkNameDuplication(String name, long id);
    boolean descriptionInUse(String description);
    boolean checkDescriptionDuplication(String description, long id);
    CompletableFuture<RoleRequest> findById(long id);
    CompletableFuture<List<RoleRequest>> findRolesAll();
    CompletableFuture<RoleRequest> create(RoleRequest role);
    CompletableFuture<RoleRequest> update(RoleRequest role);
    CompletableFuture<RoleRequest> updateRolePermissions(RoleRequest role);
    void softDelete(long id, boolean isDeleted);
    void delete(long id);
}
