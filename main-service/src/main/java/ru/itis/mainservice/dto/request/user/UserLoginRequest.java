package ru.itis.mainservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserLoginRequest (
        @NotBlank(message = "Почта не может быть пустой")
        String email,
        @NotBlank(message = "Пароль не может быть пустым")
        String password
) {
}
