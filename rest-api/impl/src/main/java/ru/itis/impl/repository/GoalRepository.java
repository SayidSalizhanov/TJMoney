package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Goal;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
