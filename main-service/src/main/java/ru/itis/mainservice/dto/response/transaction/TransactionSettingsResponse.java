package ru.itis.mainservice.dto.response.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionSettingsResponse (
        Integer amount,
        String type,
        String category,
        String dateTime,
        String ownerName,
        String description
) {
}
