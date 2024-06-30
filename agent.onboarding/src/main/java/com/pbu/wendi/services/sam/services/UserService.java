package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.requests.sam.dto.LoginRequest;
import com.pbu.wendi.requests.sam.dto.UserRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    boolean exists(long id);
    boolean pfNoTaken(String pfNo);
    boolean pfNoDuplicated(String pfNo, long id);
    boolean usernameTaken(String username);
    boolean usernameDuplicated(String username, long id);
    boolean emailTaken(String email);
    boolean emailDuplicated(String email, long id);
    CompletableFuture<UserRequest> findById(long id);
    CompletableFuture<UserRequest> findByUsername(String username);
    CompletableFuture<LoginRequest> loginUser(String username);
    CompletableFuture<UserRequest> findByEmail(String email);
    CompletableFuture<List<UserRequest>> findAll();
    CompletableFuture<List<UserRequest>> findActive();
    CompletableFuture<UserRequest> findByIdWithRole(long userI);
    CompletableFuture<UserRequest> create(UserRequest user);
    void update(UserRequest user, long roleId, long branchId);
    void updatePassword(long id, String password);
    void setActiveStatus(long id, boolean active, String modifiedBy, LocalDateTime modifiedOn);
    void verifyUser(boolean verified, String modifiedBy, LocalDateTime modifiedOn, long id);
    void setLoginStatus(long id, boolean loggedIn, LocalDateTime lastLoginOn);
    void softDelete(long id, boolean status);
    void delete(long id);
}
