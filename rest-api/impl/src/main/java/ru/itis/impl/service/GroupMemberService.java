package ru.itis.impl.service;

import org.springframework.data.domain.Pageable;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;

import javax.management.relation.Relation;
import java.util.List;

public interface GroupMemberService {
    GroupMember getGroupMember(User user, Group group);
    List<GroupMember> getGroupMembers(Group group);
    List<GroupMember> getGroupMembersWithPagination(Group group, Pageable pageable);
    List<GroupMember> getByUser(User user);
    Long save(User user, Group group);
    Long saveNoAdmin(User joinedUser, Group group);
}
