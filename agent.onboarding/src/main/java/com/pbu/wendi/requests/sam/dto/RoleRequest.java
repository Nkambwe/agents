package com.pbu.wendi.requests.sam.dto;

import java.util.List;

public class RoleRequest {
    private long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private boolean isDeleted;
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    private List<SetRequest> permissions;
    public List<SetRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<SetRequest> permissions) {
        this.permissions = permissions;
    }

}

