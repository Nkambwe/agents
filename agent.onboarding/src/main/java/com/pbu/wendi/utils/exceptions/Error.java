package com.pbu.wendi.utils.exceptions;

import java.time.LocalDateTime;

public class Error {

    //field is used to ensure the compatibility of serialized objects during the deserialization process.
    private static final long serialVersionId =1L;

    private final String path;
    public String getPath() {
        return path;
    }

    private final String message;
    public String getMessage() {
        return message;
    }

    private final int httpCode;
    public int getHttpCode() {
        return httpCode;
    }

    private final LocalDateTime date;
    public LocalDateTime getDate() {
        return date;
    }

    public Error(String message, int httpCode, LocalDateTime date) {
        this.path = null;
        this.message = message;
        this.httpCode = httpCode;
        this.date = date;
    }

    public Error(String path, String message, int httpCode, LocalDateTime date) {
        this.path = path;
        this.message = message;
        this.httpCode = httpCode;
        this.date = date;
    }

}
