package com.project.ens.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ErrorResponse {
    HttpStatus status;
    String reason;
    String message;
    String stackTrace;
    ZonedDateTime timestamp;

    public ErrorResponse(HttpStatus status, String reason, String message, String stackTrace) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.stackTrace = stackTrace;
        this.timestamp = ZonedDateTime.now();
    }
}
