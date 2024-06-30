package com.pbu.wendi.utils.requests.agents.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BusinessRequest {
    private long id;
    private String sortCode;
    private String pinNumber;
    private String regNo;
    private LocalDateTime incorporatedOn;
    private String registeredName;
    //business name same as registered
    private boolean sameName;
    private String businessName;
    private String businessNature;
    private int outlets;
    private String businessTin;

    private String telephone;
    private String physicalAddress;
    private String postalAddress;
    private String postalOffice;
    private int yearsInBusiness;
    private String district;
    private String villageOrParishOrCounty;
    private String longitude;
    private String latitude;
    private int agentType;
    private int affiliationId;
    private int retailCategory;
    private boolean isDeleted;
    private ApprovalRequest approval;
    private OperatorRequest operator;
    private List<SignatoryRequest> signatories;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public LocalDateTime getIncorporatedOn() {
        return incorporatedOn;
    }

    public void setIncorporatedOn(LocalDateTime regDate) {
        this.incorporatedOn = regDate;
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public boolean isSameName() {
        return sameName;
    }

    public void setSameName(boolean sameName) {
        this.sameName = sameName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessNature() {
        return businessNature;
    }

    public void setBusinessNature(String businessNature) {
        this.businessNature = businessNature;
    }

    public int getOutlets() {
        return outlets;
    }

    public void setOutlets(int outlets) {
        this.outlets = outlets;
    }

    public String getBusinessTin() {
        return businessTin;
    }

    public void setBusinessTin(String businessTin) {
        this.businessTin = businessTin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPostalOffice() {
        return postalOffice;
    }

    public void setPostalOffice(String postalOffice) {
        this.postalOffice = postalOffice;
    }

    public int getYearsInBusiness() {
        return yearsInBusiness;
    }

    public void setYearsInBusiness(int yearsInBusiness) {
        this.yearsInBusiness = yearsInBusiness;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getVillageOrParishOrCounty() {
        return villageOrParishOrCounty;
    }

    public int getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(int affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setVillageOrParishOrCounty(String villageOrParishOrCounty) {
        this.villageOrParishOrCounty = villageOrParishOrCounty;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getAgentType() {
        return agentType;
    }

    public void setAgentType(int agentType) {
        this.agentType = agentType;
    }

    public int getRetailCategory() {
        return retailCategory;
    }

    public void setRetailCategory(int retailCategory) {
        this.retailCategory = retailCategory;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ApprovalRequest getApproval() {
        return approval;
    }

    public void setApproval(ApprovalRequest approval) {
        this.approval = approval;
    }

    public OperatorRequest getOperator() {
        return operator;
    }

    public void setOperator(OperatorRequest operator) {
        this.operator = operator;
    }

    public List<SignatoryRequest> getSignatories() {
        return signatories;
    }

    public void setSignatories(List<SignatoryRequest> signatories) {
        this.signatories = signatories;
    }
}

