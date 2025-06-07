package ru.itis.dto.request.group;

import lombok.Builder;

@Builder
public record GroupSettingsRequest (
        String name,
        String description
) {
}
