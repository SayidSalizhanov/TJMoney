package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
