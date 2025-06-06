package ru.itis.dto.request.article;

import lombok.Builder;

@Builder
public record ArticleCreateRequest(
        String title,
        String content,
        String author
) {
} 