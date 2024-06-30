package com.pbu.wendi.requests.agents.dto;

public class OperatorRequest {
    private long id;
    long agentId;
    private String operatorName;
    private int gender;
    private String phoneNo;
    private String idNin;
    private String channels;
    private String bankName;
    private String accountNo;
    private String accountName;
    private boolean hasCrime;
    private String crimeRef;
    private boolean hasActiveCrime;
    private String activeCrimeRef;

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

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIdNin() {
        return idNin;
    }

    public void setIdNin(String idNin) {
        this.idNin = idNin;
    }

    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public boolean isHasCrime() {
        return hasCrime;
    }

    public void setHasCrime(boolean hasCrime) {
        this.hasCrime = hasCrime;
    }

    public String getCrimeRef() {
        return crimeRef;
    }

    public void setCrimeRef(String crimeRef) {
        this.crimeRef = crimeRef;
    }

    public boolean isHasActiveCrime() {
        return hasActiveCrime;
    }

    public void setHasActiveCrime(boolean hasActiveCrime) {
        this.hasActiveCrime = hasActiveCrime;
    }

    public String getActiveCrimeRef() {
        return activeCrimeRef;
    }

    public void setActiveCrimeRef(String activeCrimeRef) {
        this.activeCrimeRef = activeCrimeRef;
    }

}