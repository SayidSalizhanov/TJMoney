package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
