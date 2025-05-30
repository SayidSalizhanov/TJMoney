package ru.itis.mainservice.dto.request.record;

import lombok.Builder;

@Builder
public record RecordCreateRequest (
        String title,
        String content
) {
}
