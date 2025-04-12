package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.username = :username, u.telegramId = :telegramId, u.sendingToTelegram = :sendingToTelegram, u.sendingToEmail = :sendingToEmail WHERE u.id = :id")
    void update(@Param("username") String username, @Param("telegramId") String telegramId, @Param("sendingToTelegram") Boolean sendingToTelegram, @Param("sendingToEmail") Boolean sendingToEmail, @Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("password") String password, @Param("id") Long id);
}
