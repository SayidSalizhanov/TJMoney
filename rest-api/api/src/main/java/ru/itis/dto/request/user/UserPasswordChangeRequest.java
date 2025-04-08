package ru.itis.dto.request.user;

import lombok.Builder;

@Builder
public record UserPasswordChangeRequest (
        String oldPassword,
        String newPassword,
        String repeatNewPassword
) {
}
