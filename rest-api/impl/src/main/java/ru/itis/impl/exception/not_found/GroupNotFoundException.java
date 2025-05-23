package ru.itis.impl.exception;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(String message) {
        super(message, Htt);
    }
}
