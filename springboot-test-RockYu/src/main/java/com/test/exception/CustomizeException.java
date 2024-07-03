package com.test.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomizeException extends RuntimeException {
    public CustomizeException(String message) {
        super(message);
    }
}