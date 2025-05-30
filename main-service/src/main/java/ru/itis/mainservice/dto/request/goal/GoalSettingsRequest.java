package ru.itis.mainservice.dto.request.goal;

import lombok.Builder;

@Builder
public record GoalSettingsRequest (
        String title,
        Integer progress,
        String description
) {
}
