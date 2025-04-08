package ru.itis.dto.response.application;

import lombok.Builder;

@Builder
public record ApplicationToGroupResponse (
        Long applicationId,
        String groupName,
        String sendAt,
        String status
) {
}
