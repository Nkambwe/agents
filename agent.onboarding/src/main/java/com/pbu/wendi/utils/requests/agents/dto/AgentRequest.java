package com.pbu.wendi.utils.requests.agents.dto;

import java.time.LocalDateTime;

public class AgentRequest {
    private long id;
    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private int gender;
    public long getGender(){
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }


    private long type;
    public long getType(){
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private boolean is_deleted;
    public boolean getIsDeleted(){
        return is_deleted;
    }

    public void setIsDeleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    private String firstName;
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String middleName;
    public String getMiddleName(){
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    private String lastName;
    public String getSurname(){
        return lastName;
    }

    public void setSurname(String lastName) {
        this.lastName = lastName;
    }

    private String name;
    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String tin;
    public String getTin(){
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    private String primaryContact;
    public String getPrimaryContact(){
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    private String secondaryContact;
    public String getSecondaryContact(){
        return secondaryContact;
    }

    public void setSecondaryContact(String secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    private String email;
    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String postalAddress;
    public String getPostalAddress(){return postalAddress;}
    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    private String recruitedBy;
    public String getRecruitedBy(){
        return recruitedBy;
    }

    public void setRecruitedBy(String recruitedBy) {
        this.recruitedBy = recruitedBy;
    }

    private int retail;
    public int getRetail(){
        return retail;
    }

    public void setRetail(int retail) {
        this.retail = retail;
    }

    private boolean is_retail;
    public boolean getIsRetail(){
        return is_retail;
    }

    public void setIsRetail(boolean is_retail) {
        this.is_retail = is_retail;
    }

    private int retailType;
    public int getRetailType(){
        return retailType;
    }

    public void setRetailType(int retailType) {
        this.retailType = retailType;
    }

    private int retailStatus;
    public int getRetailStatus(){
        return retailStatus;
    }

    public void setRetailStatus(int retailStatus) {
        this.retailStatus = retailStatus;
    }

    private long affiliationId;
    public long getAffiliationId(){
        return affiliationId;
    }

    public void setAffiliationId(long affiliationId) {
        this.affiliationId = affiliationId;
    }

    private String affiliationCode;
    public String getAffiliationCode(){
        return affiliationCode;
    }
    public void setAffiliationCode(String affiliationCode) {
        this.affiliationCode = affiliationCode;
    }

    private String affiliationName;
    public String getAffiliationName(){
        return affiliationName;
    }
    public void setAffiliationName(String affiliationName) {
        this.affiliationName = affiliationName;
    }

    private String recruitComment;
    public String getRecruitComment(){
        return recruitComment;
    }

    public void setRecruitComment(String recruitComment) {
        this.recruitComment = recruitComment;
    }

    private LocalDateTime recruitedOn;
    public LocalDateTime getRecruitedOn(){
        return recruitedOn;
    }

    private int recruitStatus;
    public int getRecruitStatus(){
        return recruitStatus;
    }

    public void setRecruitStatus(int recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public void setRecruitedOn(LocalDateTime recruitedOn) {
        this.recruitedOn = recruitedOn;
    }

    private String createdBy;
    public String getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private int createdStatus;
    public int getCreatedStatus(){
        return createdStatus;
    }

    public void setCreatedStatus(int createdStatus) {
        this.createdStatus = createdStatus;
    }

    private String createdComment;
    public String getCreatedComment(){
        return createdComment;
    }

    public void setCreatedComment(String createdComment) {
        this.createdComment = createdComment;
    }

    private LocalDateTime createdOn;
    public LocalDateTime getCreatedOn(){
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    private String reviewedBy;
    public String getReviewedBy(){
        return reviewedBy;
    }
    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    private LocalDateTime reviewedOn;
    public LocalDateTime getReviewedOn(){
        return reviewedOn;
    }
    public void setReviewedOn(LocalDateTime reviewedOn) {
        this.reviewedOn = reviewedOn;
    }

    private int reviewStatus;
    public int getReviewStatus(){
        return reviewStatus;
    }
    public void setReviewStatus(int reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    private String reviewComment;
    public String getReviewComment(){
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    private long approveId;
    public long getApproveId(){
        return approveId;
    }
    public void setApproveId(long approveId) {
        this.approveId = approveId;
    }

    private String approvedBy;
    public String getApprovedBy(){
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    private int approveStatus;
    public int getApproveStatus(){
        return approveStatus;
    }
    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    private LocalDateTime approvedOn;
    public LocalDateTime getApprovedOn(){
        return approvedOn;
    }
    public void setApprovedOn(LocalDateTime approvedOn) {
        this.approvedOn = approvedOn;
    }

    private String approveComment;
    public String getApproveComment(){
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    private long operatorId;
    public long getOperatorId(){
        return operatorId;
    }
    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    private String operatorName;
    public String getOperatorName(){
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    private String operatorContact;
    public String getOperatorContact(){
        return operatorContact;
    }

    public void setOperatorContact(String operatorContact) {
        this.operatorContact = operatorContact;
    }

    private String operatorIdNumber;
    public String getOperatorIdNumber(){
        return operatorIdNumber;
    }
    public void setOperatorIdNumber(String operatorIdNumber) {
        this.operatorIdNumber = operatorIdNumber;
    }

    private String operatorChannels;
    public String getOperatorChannels(){
        return operatorChannels;
    }
    public void setOperatorChannels(String operatorChannels) {
        this.operatorChannels = operatorChannels;
    }

    private String operatorBank;
    public String getOperatorBank(){
        return operatorBank;
    }

    public void setOperatorBank(String operatorBank) {
        this.operatorBank = operatorBank;
    }

    private String accountNumber;
    public String getAccountNumber(){
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    private String accountName;
    public String getAccountName(){
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}

