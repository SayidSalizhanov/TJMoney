package ru.itis.mainservice.dto.response.group;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
public record GroupProfileResponse (
        List<Map<String, Integer>> transactionsGenerals,
        String userRole,
        Long id,
        String name,
        String createdAt,
        String description
) {
}
