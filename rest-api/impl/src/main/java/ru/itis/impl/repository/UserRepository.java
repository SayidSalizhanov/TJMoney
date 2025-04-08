package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
