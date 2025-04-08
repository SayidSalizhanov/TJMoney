package ru.itis.dto.response.reminder;

import lombok.Builder;

@Builder
public record ReminderListResponse (
        Long id,
        String title,
        String status
) {
}
