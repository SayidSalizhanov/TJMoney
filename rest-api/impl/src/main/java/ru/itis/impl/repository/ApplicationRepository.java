package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByGroupAndStatus(Group group, String status);
    void deleteByGroupAndUser(Group group, User user);
}
