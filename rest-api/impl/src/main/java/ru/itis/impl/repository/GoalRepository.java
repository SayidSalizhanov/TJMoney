package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Goal;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.group IS NULL")
    Page<Goal> findByUser(@Param("user") User user, Pageable pageable);

    Page<Goal> findByGroup(Group group, Pageable pageable);

    @Modifying
    @Query("UPDATE Goal g SET g.title = :title, g.description = :description, g.progress = :progress WHERE g.id = :id")
    void update(@Param("title") String title, @Param("description") String description, @Param("progress") Integer progress, @Param("id") Long id);
}
