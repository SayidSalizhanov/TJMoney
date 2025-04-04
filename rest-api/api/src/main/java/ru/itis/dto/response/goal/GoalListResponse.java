package ru.itis.dto.response.goal;

import lombok.Builder;

@Builder
public record GoalListResponse (Long id, String title, String progress) {
}
