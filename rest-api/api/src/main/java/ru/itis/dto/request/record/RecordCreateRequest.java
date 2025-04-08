package ru.itis.dto.request.record;

import lombok.Builder;

@Builder
public record RecordCreateRequest (
        String title,
        String content
) {
}
