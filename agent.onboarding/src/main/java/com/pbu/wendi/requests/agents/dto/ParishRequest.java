package com.pbu.wendi.requests.agents.dto;

public class ParishRequest {
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    private String parishName;
    public String getParishName() {
        return parishName;
    }

    public void setParishName(String parishName) {
        this.parishName = parishName;
    }

    private boolean isDeleted;
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
