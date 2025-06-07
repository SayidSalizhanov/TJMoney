package ru.itis.dto.response.group;

import lombok.Builder;

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
