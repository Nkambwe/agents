package com.pbu.wendi.requests.agents.dto;

public class CountyRequest {
    private long id;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    private String countyName;
    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    private boolean isDeleted;
    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
