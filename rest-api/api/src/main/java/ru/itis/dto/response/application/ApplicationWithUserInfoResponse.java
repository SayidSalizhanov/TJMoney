package ru.itis.dto.response.application;

import lombok.Builder;

@Builder
public record ApplicationWithUserInfoResponse (
        Long applicationId,
        Long userId,
        String username,
        String sendAt
) {
}
