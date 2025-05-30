package ru.itis.mainservice.dto.request.user;

import lombok.Builder;

@Builder
public record UserLoginRequest (
        String email,
        String password
) {
}
