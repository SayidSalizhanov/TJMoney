package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    Page<Group> findByIdNotIn(List<Long> excludedIds, Pageable pageable);

    @Modifying
    @Query("UPDATE Group g SET g.name = :name, g.description = :description WHERE g.id = :id")
    void update(@Param("name") String name, @Param("description") String description, @Param("id") Long id);
}
