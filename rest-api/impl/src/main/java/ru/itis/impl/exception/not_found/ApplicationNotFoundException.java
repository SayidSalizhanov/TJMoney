package ru.itis.impl.exception.not_found;

import org.springframework.http.HttpStatus;
import ru.itis.impl.exception.ServiceException;

public class ApplicationNotFoundException extends ServiceException {
    public ApplicationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
