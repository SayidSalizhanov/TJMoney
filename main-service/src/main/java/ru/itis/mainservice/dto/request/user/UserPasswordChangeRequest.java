package ru.itis.mainservice.dto.request.user;

import lombok.Builder;

@Builder
public record UserPasswordChangeRequest (
        String oldPassword,
        String newPassword,
        String repeatNewPassword
) {
}
