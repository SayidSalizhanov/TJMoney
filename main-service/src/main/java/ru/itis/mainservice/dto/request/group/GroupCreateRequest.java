package ru.itis.mainservice.dto.request.group;

import lombok.Builder;

@Builder
public record GroupCreateRequest (
        String name,
        String description
) {
}
