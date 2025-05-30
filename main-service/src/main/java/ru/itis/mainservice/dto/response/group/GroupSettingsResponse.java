package ru.itis.mainservice.dto.response.group;

import lombok.Builder;

@Builder
public record GroupSettingsResponse (
        Long id,
        String name,
        String description
) {
}
