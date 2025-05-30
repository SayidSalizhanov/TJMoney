package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
}
