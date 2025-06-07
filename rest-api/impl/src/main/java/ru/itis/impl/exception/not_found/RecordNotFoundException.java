package ru.itis.impl.exception.not_found;

import org.springframework.http.HttpStatus;
import ru.itis.impl.exception.ServiceException;

public class RecordNotFoundException extends ServiceException {
    public RecordNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
