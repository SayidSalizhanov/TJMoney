package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Record;
import ru.itis.impl.model.User;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findAllByUserAndGroupIsNull(User user, Pageable pageable);
    Page<Record> findAllByGroup(Group group, Pageable pageable);
}
