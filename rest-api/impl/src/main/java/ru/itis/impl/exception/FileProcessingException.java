package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class FileProcessingException extends ServiceException {
    public FileProcessingException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
