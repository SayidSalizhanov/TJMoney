package ru.itis.mainservice.dto.request.group;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record GroupCreateRequest (
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 50, message = "Название должно быть от 3 до 50 символов")
        String name,

        @Size(max = 1000, message = "Описание не может быть длиннее 1000 символов")
        String description
) {
}
