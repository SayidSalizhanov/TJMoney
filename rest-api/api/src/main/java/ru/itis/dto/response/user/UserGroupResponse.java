package ru.itis.dto.response.user;

import lombok.Builder;

@Builder
public record UserGroupResponse (
        Long groupId,
        String groupName,
        String description,
        String role
) {
}
