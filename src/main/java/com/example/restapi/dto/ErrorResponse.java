package com.example.restapi.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String status;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String status,
                         String message,
                         String errorCode,
                         LocalDateTime timestamp) {

        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}