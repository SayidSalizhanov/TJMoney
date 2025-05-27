package ru.itis.dto.response.group;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GroupMemberResponse (
        Long userId,
        String username,
        LocalDateTime joinedAt,
        String role
) {
}
