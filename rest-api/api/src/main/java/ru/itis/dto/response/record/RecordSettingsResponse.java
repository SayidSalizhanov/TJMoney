package ru.itis.dto.response.record;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RecordSettingsResponse (
        String title,
        String content,
        String createdAt,
        String updatedAt
) {
}
