package ru.itis.mainservice.dto.request.transaction;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record TransactionSettingsRequest (
        @NotNull(message = "Сумма не может быть пустой")
        @Min(value = 0, message = "Сумма не может быть отрицательной")
        @Max(value = 1000000, message = "Сумма не может быть больше 1 000 000")
        Integer amount,

        @NotNull(message = "Тип не может быть пустым")
        @Pattern(regexp = "^(Доход|Расход)$", message = "Тип должен быть 'Доход' или 'Расход'")
        String type,

        @NotNull(message = "Категория не может быть пустой")
        @NotBlank(message = "Категория не может быть пустой")
        String category,

        @NotNull(message = "Описание не может быть пустым")
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 1000, message = "Описание не может быть длиннее 1000 символов")
        String description
) {
}
