package ru.itis.dto.request.record;

import lombok.Builder;

@Builder
public record RecordSettingsRequest (String title, String content) {
}
