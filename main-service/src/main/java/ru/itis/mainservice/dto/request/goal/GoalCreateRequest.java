package ru.itis.mainservice.dto.request.goal;

import lombok.Builder;

@Builder
public record GoalCreateRequest (
        String title,
        String description,
        Integer progress
) {
}
