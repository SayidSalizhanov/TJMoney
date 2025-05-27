package ru.itis.dto.response.group;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupViewingResponse (
        Long id,
        String name,
        LocalDateTime createdAt,
        String description,
        Integer membersCount,
        String adminName
) {
}
