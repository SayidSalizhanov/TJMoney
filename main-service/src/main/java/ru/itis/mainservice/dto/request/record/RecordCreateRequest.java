package ru.itis.mainservice.dto.request.record;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record RecordCreateRequest (
        @NotNull(message = "Название не может быть пустым")
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String title,

        @NotNull(message = "Содержание не может быть пустым")
        @NotBlank(message = "Содержание не может быть пустым")
        @Size(max = 10000, message = "Содержание не может быть длиннее 10000 символов")
        String content
) {
}
