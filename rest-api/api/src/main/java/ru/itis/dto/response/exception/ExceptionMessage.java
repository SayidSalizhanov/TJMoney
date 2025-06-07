package ru.itis.dto.response.exception;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record ExceptionMessage(
        String message,
        Map<String, List<String>> errors,
        String exceptionName) {
}