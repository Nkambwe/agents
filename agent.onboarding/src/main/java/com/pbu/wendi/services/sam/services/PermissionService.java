package com.pbu.wendi.services.sam.services;


import com.pbu.wendi.requests.sam.dto.PermissionRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PermissionService {

    boolean nameInUse(String name);
    boolean checkNameDuplication(String name, long id);
    boolean descriptionInUse(String description);
    boolean checkDescriptionDuplication(String description, long id);
    CompletableFuture<PermissionRequest> findById(long id);
    CompletableFuture<List<PermissionRequest>> findPermissions();
    CompletableFuture<List<PermissionRequest>> findPermissions(long setId);
    CompletableFuture<List<PermissionRequest>> findPermissionsByRoleId(Long roleId);
    void lockSetPermissions(long setId, boolean isLocked);
    void update(PermissionRequest permission);
}
