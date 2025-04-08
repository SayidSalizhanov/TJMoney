package ru.itis.dto.response.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionSettingsResponse (
        Integer amount,
        String type,
        String category,
        LocalDateTime dateTime,
        String ownerName,
        String description
) {
}
