package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.mainservice.dto.response.article.ArticleMainPageResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    public List<ArticleMainPageResponse> getAllArticles(Integer page, Integer amountPerPage) {
        String url = String.format("%s/api/articles?page=%d&amount_per_page=%d",
                apiBaseUrl, page, amountPerPage);

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ArticleMainPageResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ArticleMainPageResponse[].class
        );

        return Optional.ofNullable(response.getBody())
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }
} 