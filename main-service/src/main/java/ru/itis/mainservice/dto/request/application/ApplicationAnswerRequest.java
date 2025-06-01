package ru.itis.mainservice.dto.request.application;

import lombok.Builder;

@Builder
public record ApplicationAnswerRequest (
        Long applicationId,
        Long userForJoinId,
        String applicationStatus
) {
}
