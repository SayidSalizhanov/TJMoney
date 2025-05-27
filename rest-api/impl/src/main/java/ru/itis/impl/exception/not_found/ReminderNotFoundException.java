package ru.itis.impl.exception.not_found;

import org.springframework.http.HttpStatus;
import ru.itis.impl.exception.ServiceException;

public class ReminderNotFoundException extends ServiceException {
    public ReminderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
