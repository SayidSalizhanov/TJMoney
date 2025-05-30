package ru.itis.impl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    void deleteByGroupAndUser(Group group, User user);
    Optional<GroupMember> findByGroupAndUser(Group group, User user);
    List<GroupMember> findAllByGroup(Group group);
    Page<GroupMember> findAllByGroup(Group group, Pageable pageable);
    List<GroupMember> findAllByUser(User user);
    List<GroupMember> findByUserAndRole(User user, String role);
}
