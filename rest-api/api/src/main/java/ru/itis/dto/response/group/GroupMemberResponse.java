package ru.itis.dto.response.group;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupMemberResponse (
        String username,
        LocalDateTime joinedAt,
        String role
) {
}
