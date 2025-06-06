package ru.itis.mainservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserSettingsRequest(
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя пользователя должно быть от 2 до 50 символов")
    String username,

    @Size(max = 100, message = "Telegram ID не может быть длиннее 100 символов")
    String telegramId,

    boolean sendingToEmail
) {
}
