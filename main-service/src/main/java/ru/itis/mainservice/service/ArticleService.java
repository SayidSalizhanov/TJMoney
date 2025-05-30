package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.mainservice.dto.response.article.ArticleMainPageResponse;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    public List<ArticleMainPageResponse> getAllArticles(Integer page, Integer amountPerPage) {
        String url = String.format("%s/api/articles?page=%d&amount_per_page=%d",
                apiBaseUrl, page, amountPerPage);
        
        ArticleMainPageResponse[] articles = restTemplate.getForObject(url, ArticleMainPageResponse[].class);
        return articles != null ? Arrays.asList(articles) : List.of();
    }
} 