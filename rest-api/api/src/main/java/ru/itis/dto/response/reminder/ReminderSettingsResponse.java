package ru.itis.dto.response.reminder;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReminderSettingsResponse (
        String title,
        String sendAt,
        String status,
        String message
) {
}
