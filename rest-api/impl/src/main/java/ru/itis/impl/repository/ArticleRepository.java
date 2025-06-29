package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
