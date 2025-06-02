package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.impl.model.ERole;
import ru.itis.impl.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
