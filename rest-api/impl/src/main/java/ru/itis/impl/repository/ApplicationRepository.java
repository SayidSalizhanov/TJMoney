package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Modifying
    @Query("UPDATE Application a SET a.status = :status WHERE a.id = :applicationId")
    void updateStatus(@Param("applicationId") Long applicationId, @Param("status") String status);

    List<Application> findByUser(User user);

    List<Application> findByGroup(Group group);

    void deleteByGroupAndUser(Group group, User user);
}
