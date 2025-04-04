package ru.itis.dto.request.user;

import lombok.Builder;

@Builder
public record UserRegisterRequest (String username, String email, String password, String confirmPassword) {
}
