package ru.itis.dto.response.application;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ApplicationWithUserInfoResponse (
        Long applicationId,
        String username,
        LocalDateTime sendAt
) {
}
