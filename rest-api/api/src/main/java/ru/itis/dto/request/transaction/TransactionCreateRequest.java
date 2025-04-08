package ru.itis.dto.request.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionCreateRequest (
        Integer amount,
        String type,
        String category,
        LocalDateTime dateTime,
        String description
) {
}
