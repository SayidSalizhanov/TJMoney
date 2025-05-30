package ru.itis.mainservice.dto.request.transaction;

import lombok.Builder;

@Builder
public record TransactionSettingsRequest (
        Integer amount,
        String type,
        String category,
        String description
) {
}
