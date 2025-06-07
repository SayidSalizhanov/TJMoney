package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.article.ArticleCreateRequest;
import ru.itis.dto.response.article.ArticleMainPageResponse;

import java.util.List;

@RequestMapping("/api/articles")
public interface ArticleApi {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ArticleMainPageResponse> getArticles(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @PostMapping("/admin/create")
    @ResponseStatus(HttpStatus.CREATED)
    Long createArticle(@RequestBody ArticleCreateRequest request);
}
