package ru.itis.dto.request.group;

import lombok.Builder;

@Builder
public record GroupCreateRequest (
        String name,
        String description
) {
}
