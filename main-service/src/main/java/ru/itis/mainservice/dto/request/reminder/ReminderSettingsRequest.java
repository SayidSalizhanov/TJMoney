package ru.itis.mainservice.dto.request.reminder;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReminderSettingsRequest (
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String title,

        @NotNull(message = "Время отправки не может быть пустым")
        @Future(message = "Время отправки должно быть в будущем")
        LocalDateTime sendAt,

        @NotNull(message = "Сообщение не может быть пустым")
        @NotBlank(message = "Сообщение не может быть пустым")
        @Size(max = 1000, message = "Сообщение не может быть длиннее 1000 символов")
        String message
) {
}
