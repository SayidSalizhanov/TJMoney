package ru.itis.dto.response.article;

import lombok.Builder;

@Builder
public record ArticleMainPageResponse (
        String title,
        String content,
        String author,
        String publishedAt
) {
}
