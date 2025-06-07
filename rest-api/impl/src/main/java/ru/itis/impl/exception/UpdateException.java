package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class UpdateException extends ServiceException {
    public UpdateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
