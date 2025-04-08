package ru.itis.dto.response.reminder;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReminderSettingsResponse (
        String title,
        LocalDateTime sendAt,
        String status,
        String message
) {
}
