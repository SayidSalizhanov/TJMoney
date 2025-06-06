package ru.itis.dto.request.user;

import lombok.Builder;

@Builder
public record UserPasswordChangeRequest (
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}
