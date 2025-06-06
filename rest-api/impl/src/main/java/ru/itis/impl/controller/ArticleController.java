package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.ArticleApi;
import ru.itis.dto.request.article.ArticleCreateRequest;
import ru.itis.dto.response.article.ArticleMainPageResponse;
import ru.itis.impl.service.ArticleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController implements ArticleApi {

    private final ArticleService articleService;

    @Override
    public List<ArticleMainPageResponse> getArticles(Integer page, Integer amountPerPage) {
        return articleService.getAll(page, amountPerPage);
    }

    @Override
    public Long createArticle(ArticleCreateRequest request) {
        return articleService.create(request);
    }
}
