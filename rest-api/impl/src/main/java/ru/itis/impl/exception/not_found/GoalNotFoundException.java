package ru.itis.impl.exception;

import org.springframework.http.HttpStatus;

public class GoalNotFoundException extends ServiceException {
    public GoalNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
