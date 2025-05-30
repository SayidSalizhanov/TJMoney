package ru.itis.mainservice.dto.response.record;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordSettingsResponse (
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
