package ru.itis.mainservice.dto.request.goal;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record GoalCreateRequest (
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String title,

        @Size(max = 1000, message = "Описание не может быть длиннее 1000 символов")
        String description,

        @NotNull(message = "Прогресс не может быть пустым")
        @Min(value = 0, message = "Прогресс не может быть меньше 0")
        @Max(value = 100, message = "Прогресс не может быть больше 100")
        Integer progress
) {
}
