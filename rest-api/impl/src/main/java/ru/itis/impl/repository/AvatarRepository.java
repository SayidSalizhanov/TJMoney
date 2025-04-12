package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Avatar;
import ru.itis.impl.model.User;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    @Modifying
    @Query("UPDATE Avatar a SET a.url = :url WHERE a.user = :user")
    void update(@Param("url") String url, @Param("user") User user);

    @Query("SELECT a FROM Avatar a WHERE a.user = :user")
    String findUrl(@Param("user") User user);
}
