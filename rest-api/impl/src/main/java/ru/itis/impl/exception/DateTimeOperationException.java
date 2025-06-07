package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class DateTimeOperationException extends ServiceException {
    public DateTimeOperationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
