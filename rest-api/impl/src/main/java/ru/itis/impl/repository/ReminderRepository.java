package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
