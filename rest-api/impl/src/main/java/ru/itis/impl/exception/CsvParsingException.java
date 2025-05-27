package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class CsvParsingException extends ServiceException {
    public CsvParsingException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
