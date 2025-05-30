package ru.itis.mainservice.dto.request.reminder;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReminderCreateRequest (
        String title,
        LocalDateTime sendAt,
        String message
) {
}
