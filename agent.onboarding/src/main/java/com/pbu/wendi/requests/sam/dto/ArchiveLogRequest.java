package com.pbu.wendi.requests.sam.dto;

import java.time.LocalDateTime;

public class ArchiveLogRequest {
    private long id;
    private String action;
    private LocalDateTime logTime;
    private String ipAddress;
    private String logForUser;
    private LocalDateTime archivedOn;
    private String user;
    private boolean isDeleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLogForUser() {
        return logForUser;
    }

    public void setLogForUser(String logForUser) {
        this.logForUser = logForUser;
    }

    public LocalDateTime getArchivedOn() {
        return archivedOn;
    }

    public void setArchivedOn(LocalDateTime archivedOn) {
        this.archivedOn = archivedOn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

