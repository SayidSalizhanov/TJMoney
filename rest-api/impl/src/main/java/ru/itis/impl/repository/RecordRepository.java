package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Record;
import ru.itis.impl.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("SELECT r FROM Record r WHERE r.user = :user AND r.group IS NULL")
    List<Record> findByUser(@Param("user") User user);

    List<Record> findByGroup(Group group);

    @Modifying
    @Query("UPDATE Record r SET r.title = :title, r.content = :content, r.updatedAt = :updatedAt WHERE r.id = :id")
    void update(@Param("title") String title, @Param("content") String content, @Param("updatedAt") LocalDateTime updatedAt, @Param("id") Long id);
}
