package ru.itis.mainservice.dto.response.group;

import lombok.Builder;

@Builder
public record GroupListResponse (
        Long id,
        String name
) {
}
