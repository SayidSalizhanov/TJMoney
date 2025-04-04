package ru.itis.dto.response.group;

import lombok.Builder;

@Builder
public record GroupSettingsResponse (Long id, String name, String description) {
}
