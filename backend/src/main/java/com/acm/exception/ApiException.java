package com.acm.exception;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final List<String> errors;

    public ApiException(HttpStatus status, String message) { this(status, message, List.of()); }

    public ApiException(HttpStatus status, String message, List<String> errors) {
        super(message);
        this.status = status;
        this.errors = errors;
    }

    public HttpStatus getStatus() { return status; }
    public List<String> getErrors() { return errors; }
}