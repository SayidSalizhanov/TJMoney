package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.mainservice.dto.request.article.ArticleCreateRequest;

@Service
@RequiredArgsConstructor
public class AdminArticleService {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    public void createArticle(ArticleCreateRequest request) {
        String url = apiBaseUrl + "/api/articles/admin/create";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ArticleCreateRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }
} 