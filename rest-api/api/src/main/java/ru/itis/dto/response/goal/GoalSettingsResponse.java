package ru.itis.dto.response.goal;

import lombok.Builder;

@Builder
public record GoalSettingsResponse (
        String title,
        String description,
        Integer progress
) {
}
