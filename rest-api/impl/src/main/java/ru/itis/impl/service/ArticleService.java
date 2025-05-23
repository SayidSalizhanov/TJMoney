package ru.itis.impl.service;

import ru.itis.dto.response.article.ArticleMainPageResponse;

import java.util.List;

public interface ArticleService {
    List<ArticleMainPageResponse> getAll(Integer page, Integer amountPerPage, String sort);
}
