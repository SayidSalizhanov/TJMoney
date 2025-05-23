package ru.itis.impl.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.response.article.ArticleMainPageResponse;
import ru.itis.impl.config.TestRestTemplateConfig;
import ru.itis.impl.mapper.ArticleMapper;
import ru.itis.impl.repository.ArticleRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(classes = TestRestTemplateConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@Sql(
        scripts = "/db/dml/insert-test-articles.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
public class ArticleControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void testGetArticles() throws Exception {
        //given
        String url = "/articles";

        //when
        ResponseEntity<List<ArticleMainPageResponse>> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        //then
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        List<ArticleMainPageResponse> expectedResult = articleRepository.findAll(pageable).getContent().stream()
                .map(a -> articleMapper.toArticleMainPageResponse(a))
                .toList();

        assertEquals(expectedResult, response.getBody());
    }
}
