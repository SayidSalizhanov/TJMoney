package ru.itis.mainservice.dto.response.application;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplicationWithUserInfoResponse (
        Long applicationId,
        Long userId,
        String username,
        LocalDateTime sendAt
) {
}
