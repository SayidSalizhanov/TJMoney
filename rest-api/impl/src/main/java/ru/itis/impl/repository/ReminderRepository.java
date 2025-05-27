package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Reminder;
import ru.itis.impl.model.User;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    @Query("SELECT r FROM Reminder r WHERE r.user = :user AND r.group IS NULL")
    List<Reminder> findByUser(@Param("user") User user);
    List<Reminder> findByGroup(Group group);
}
