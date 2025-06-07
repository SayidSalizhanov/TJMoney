package ru.itis.dto.response.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionSettingsResponse (
        Long groupId,
        Integer amount,
        String type,
        String category,
        String dateTime,
        String ownerName,
        String description
) {
}
