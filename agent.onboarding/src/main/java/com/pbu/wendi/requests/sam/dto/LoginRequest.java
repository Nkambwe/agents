package com.pbu.wendi.requests.sam.dto;

import java.time.LocalDateTime;
import java.util.List;

public class LoginRequest {
    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private String pfNo;
    private String email;
    private String password;
    private boolean isLoggedIn;
    private boolean isActive;
    private boolean isVerified;
    private boolean expirePassword;
    private int expiresIn;
    private int timeout;
    private long roleId;
    private String roleName;
    private long branchId;
    private String branchSoleId;
    private String branchName;
    private LocalDateTime lastPasswordChange;
    private List<String> permissions;
    private List<String> apps;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBranchSoleId() {
        return branchSoleId;
    }

    public void setBranchSoleId(String branchSoleId) {
        this.branchSoleId = branchSoleId;
    }

    public LocalDateTime getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(LocalDateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public String getPfNo() {
        return pfNo;
    }

    public void setPfNo(String pfNo) {
        this.pfNo = pfNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isExpirePassword() {
        return expirePassword;
    }

    public void setExpirePassword(boolean expirePassword) {
        this.expirePassword = expirePassword;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public String getBranchSolId() {
        return branchSoleId;
    }

    public void setBranchSolId(String branchSoleId) {
        this.branchSoleId = branchSoleId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }
}
