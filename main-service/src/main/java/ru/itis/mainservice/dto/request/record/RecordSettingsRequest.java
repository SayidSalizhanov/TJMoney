package ru.itis.mainservice.dto.request.record;

import lombok.Builder;

@Builder
public record RecordSettingsRequest (
        String title,
        String content
) {
}
