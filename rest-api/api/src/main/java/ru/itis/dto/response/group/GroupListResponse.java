package ru.itis.dto.response.group;

import lombok.Builder;

@Builder
public record GroupListResponse (
        Long id,
        String name
) {
}
