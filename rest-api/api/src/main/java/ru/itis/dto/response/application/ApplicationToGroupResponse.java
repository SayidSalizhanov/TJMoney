package ru.itis.dto.response.application;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplicationToGroupResponse (
        Long applicationId,
        String groupName,
        LocalDateTime sendAt,
        String status
) {
}
