package com.pbu.wendi.utils.requests.agents.dto;

public class BusinessNatureRequest {
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    private String nature;
    public String getNature() {
        return nature;
    }
    public void setNature(String countyName) {
        this.nature = nature;
    }

    private boolean isDeleted;
    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
