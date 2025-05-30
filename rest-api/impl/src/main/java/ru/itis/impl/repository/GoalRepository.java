package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Goal;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    Page<Goal> findAllByUserAndGroupIsNull(User user, Pageable pageable);
    Page<Goal> findAllByGroup(Group group, Pageable pageable);
}
