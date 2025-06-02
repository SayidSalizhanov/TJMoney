package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class RegistrationServiceException extends ServiceException {
    public RegistrationServiceException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
