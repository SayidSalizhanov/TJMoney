package ru.itis.mainservice.dto.response.goal;

import lombok.Builder;

@Builder
public record GoalListResponse (
        Long id,
        String title,
        Integer progress
) {
}
