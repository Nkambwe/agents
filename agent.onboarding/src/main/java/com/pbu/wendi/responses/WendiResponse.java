package com.pbu.wendi.responses;

public class WendiResponse {
    private int status;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    private String message;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String data;
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
