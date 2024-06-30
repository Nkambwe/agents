package com.pbu.wendi.utils.requests.agents.dto;

import java.time.LocalDateTime;

public class SignatoryRequest {
    private long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long agentId;
    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    private String fullName;
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String signatureUrl;
    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    private LocalDateTime signedOn;
    public LocalDateTime getSignedOn() {
        return signedOn;
    }

    public void setSignedOn(LocalDateTime signedOn) {
        this.signedOn = signedOn;
    }
}
