package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.itis.dto.response.article.ArticleMainPageResponse;
import ru.itis.impl.mapper.ArticleMapper;
import ru.itis.impl.repository.ArticleRepository;
import ru.itis.impl.service.ArticleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public List<ArticleMainPageResponse> getAll(Integer page, Integer amountPerPage) {
        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("title"));

        return articleRepository.findAll(pageable).getContent().stream()
                .map(articleMapper::toArticleMainPageResponse)
                .toList();
    }
}
