package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.article.ArticleMainPageResponse;
import ru.itis.impl.model.Article;

import java.time.format.DateTimeFormatter;

@Component
public class ArticleMapper {

    public ArticleMainPageResponse toArticleMainPageResponse(Article article) {
        return ArticleMainPageResponse.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .publishedAt(article.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
