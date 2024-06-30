package com.pbu.wendi.utils.requests.agents.dto;

import java.time.LocalDateTime;

public class ApprovalRequest {
    private long id;
    private long agentId;
    private String recruitedBy;
    private LocalDateTime recruitedOn;
    private int recruitStatus;
    private String recruitComment;
    private String createdBy;
    private LocalDateTime createdOn;
    private int createdStatus;
    private String createdComment;
    private String reviewedBy;
    private LocalDateTime reviewedOn;
    private int reviewStatus;
    private String reviewComment;
    private String approvedBy;
    private int approveStatus;
    private LocalDateTime approvedOn;
    private String approveComment;
    private boolean isDeleted;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgentId() {
        return agentId;
    }

    public void setAgentId(long agentId) {
        this.agentId = agentId;
    }

    public String getRecruitedBy() {
        return recruitedBy;
    }

    public void setRecruitedBy(String recruitedBy) {
        this.recruitedBy = recruitedBy;
    }

    public LocalDateTime getRecruitedOn() {
        return recruitedOn;
    }

    public void setRecruitedOn(LocalDateTime recruitedOn) {
        this.recruitedOn = recruitedOn;
    }

    public int getRecruitStatus() {
        return recruitStatus;
    }

    public void setRecruitStatus(int recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public String getRecruitComment() {
        return recruitComment;
    }

    public void setRecruitComment(String recruitComment) {
        this.recruitComment = recruitComment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public int getCreatedStatus() {
        return createdStatus;
    }

    public void setCreatedStatus(int createdStatus) {
        this.createdStatus = createdStatus;
    }

    public String getCreatedComment() {
        return createdComment;
    }

    public void setCreatedComment(String createdComment) {
        this.createdComment = createdComment;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public LocalDateTime getReviewedOn() {
        return reviewedOn;
    }

    public void setReviewedOn(LocalDateTime reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    public int getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedOn() {
        return approvedOn;
    }

    public void setApprovedOn(LocalDateTime approvedOn) {
        this.approvedOn = approvedOn;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

