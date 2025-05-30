package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Reminder;
import ru.itis.impl.model.User;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    Page<Reminder> findAllByUserAndGroupIsNull(User user, Pageable pageable);
    Page<Reminder> findAllByGroup(Group group, Pageable pageable);
}
