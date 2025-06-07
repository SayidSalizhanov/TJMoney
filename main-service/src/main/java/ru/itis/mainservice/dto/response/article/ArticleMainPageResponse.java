package ru.itis.mainservice.dto.response.article;

import lombok.Builder;

@Builder
public record ArticleMainPageResponse (
        String title,
        String content,
        String author,
        String publishedAt
) {
}
