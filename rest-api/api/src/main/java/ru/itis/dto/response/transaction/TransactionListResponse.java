package ru.itis.dto.response.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionListResponse (
        Long id,
        Integer amount,
        String username,
        String dateTime,
        String category,
        String type
) {
}
