package com.pbu.wendi.utils.requests.attachments.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class AttachmentRequest {

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

    private String attDescr;
    public String getAttDescr() {
        return attDescr;
    }

    public void setAttDescr(String attDescr) {
        this.attDescr = attDescr;
    }

    private LocalDateTime issueDate;
    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    private LocalDateTime expiryDate;
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    private int docType;
    public int getDocType() {
        return docType;
    }

    public void setDocType(int docType) {
        this.docType = docType;
    }

    private boolean isDeleted;
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    private String fileUrl;
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFile_url(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private String Data;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
