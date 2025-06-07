package ru.itis.impl.exception.not_found;

import org.springframework.http.HttpStatus;
import ru.itis.impl.exception.ServiceException;

public class GroupNotFoundException extends ServiceException {
    public GroupNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
