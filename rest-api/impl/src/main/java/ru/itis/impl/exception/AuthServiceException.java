package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class AuthServiceException extends ServiceException {
    public AuthServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
