package ru.itis.mainservice.dto.response.record;

import lombok.Builder;

@Builder
public record RecordListResponse (
        Long id,
        String title
) {
}
