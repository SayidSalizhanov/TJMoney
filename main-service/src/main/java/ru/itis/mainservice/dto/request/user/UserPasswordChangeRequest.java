package ru.itis.mainservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserPasswordChangeRequest(
    @NotBlank(message = "Текущий пароль не может быть пустым")
    String currentPassword,

    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Новый пароль должен содержать минимум 8 символов")
    String newPassword,

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    String confirmPassword
) {
}
