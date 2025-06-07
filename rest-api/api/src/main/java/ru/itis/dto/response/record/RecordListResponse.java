package ru.itis.dto.response.record;

import lombok.Builder;

@Builder
public record RecordListResponse (
        Long id,
        String title
) {
}
