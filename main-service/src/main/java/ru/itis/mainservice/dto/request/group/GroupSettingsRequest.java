package ru.itis.mainservice.dto.request.group;

import lombok.Builder;

@Builder
public record GroupSettingsRequest (
        String name,
        String description
) {
}
