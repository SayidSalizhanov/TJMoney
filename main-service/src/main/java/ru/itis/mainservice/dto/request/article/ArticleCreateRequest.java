package ru.itis.mainservice.dto.request.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ArticleCreateRequest(
        @NotBlank(message = "Название не может быть пустым")
        @Size(min = 3, max = 100, message = "Название должно быть от 3 до 100 символов")
        String title,

        @NotBlank(message = "Содержание не может быть пустым")
        @Size(min = 10, max = 10000, message = "Содержание должно быть от 10 до 10000 символов")
        String content,

        @NotBlank(message = "Автор не может быть пустым")
        @Size(min = 2, max = 50, message = "Имя автора должно быть от 2 до 50 символов")
        String author
) {
} 