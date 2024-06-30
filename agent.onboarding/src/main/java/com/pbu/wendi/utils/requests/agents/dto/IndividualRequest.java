package com.pbu.wendi.utils.requests.agents.dto;

import java.time.LocalDateTime;
import java.util.List;

public class IndividualRequest {
    private long id;
    private String firstName;
    private String middleName;
    private String surname;
    private LocalDateTime birthDate;
    private int gender;
    private String tin;
    private String sortCode;
    private String pinNumber;
    private String primaryContact;
    private String secondaryContact;
    private String postalAddress;
    private String email;
    private String operatorName;
    private boolean isSameName;
    private int agentType;
    private int category;
    private int retailCategory;
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
    private long approveId;
    private String approvedBy;
    private int approveStatus;
    private LocalDateTime approvedOn;
    private String approveComment;
    private boolean isDeleted;

    private long affiliationId;

    public long getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(long affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    private long identificationId;
    public long getIdentificationId() {
        return identificationId;
    }

    public void setIdentificationId(long identificationId) {
        this.identificationId = identificationId;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getSecondaryContact() {
        return secondaryContact;
    }

    public void setSecondaryContact(String secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAgentType() {
        return agentType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public boolean isSame() {
        return isSameName;
    }

    public void setIsSame(boolean isSameName) {
        this.isSameName = isSameName;
    }
    public boolean isDeleted() {
        return isDeleted;
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

    public long getApproveId() {
        return approveId;
    }

    public void setApproveId(long approveId) {
        this.approveId = approveId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
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

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    private boolean isRetail;
    public boolean isRetail() {
        return isRetail;
    }

    public void setIsRetail(boolean isRetail) {
        this.isRetail = isRetail;
    }

    public int getRetailCategory() {
        return retailCategory;
    }

    public void setRetailCategory(int retailCategory) {
        this.retailCategory = retailCategory;
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

    private OperatorRequest operator;

    public OperatorRequest getOperator() {
        return operator;
    }

    public void setOperator(OperatorRequest operator) {
        this.operator = operator;
    }

    private List<SignatoryRequest> signatories;
    public List<SignatoryRequest> getSignatories() {
        return signatories;
    }

    public void setSignatories(List<SignatoryRequest> signatories) {
        this.signatories = signatories;
    }

    private List<KinRequest> relatives;
    public List<KinRequest> getRelatives() {
        return relatives;
    }
    public void setRelatives(List<KinRequest> relatives) {
        this.relatives = relatives;
    }

    private List<PartnerRequest> partners;
    public List<PartnerRequest> getPartners() {
        return partners;
    }
    public void setPartners(List<PartnerRequest> partners) {
        this.partners = partners;
    }

}
