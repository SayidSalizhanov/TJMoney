package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class EmailSendingException extends ServiceException {
    public EmailSendingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 