package ru.itis.dto.request.application;

import lombok.Builder;

@Builder
public record ApplicationAnswerRequest (
        Long applicationId,
        String username,
        String applicationStatus
) {
}
