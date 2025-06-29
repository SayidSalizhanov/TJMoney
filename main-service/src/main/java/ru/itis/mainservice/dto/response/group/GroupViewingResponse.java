package ru.itis.mainservice.dto.response.group;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupViewingResponse (
        Long id,
        String name,
        String createdAt,
        String description,
        Integer membersCount,
        String adminName
) {
}
