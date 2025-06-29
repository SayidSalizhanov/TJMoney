package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.impl.exception.not_found.GroupMemberNotFoundException;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupMemberRepository;
import ru.itis.impl.service.GroupMemberService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.itis.impl.enums.GroupMemberStatusEnum.ADMIN;
import static ru.itis.impl.enums.GroupMemberStatusEnum.MEMBER;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public GroupMember getGroupMember(User user, Group group) {
        return requireByUserAndGroup(user, group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMember> getGroupMembers(Group group) {
        return groupMemberRepository.findAllByGroup(group);
    }

    @Override
    public List<GroupMember> getGroupMembersWithPagination(Group group, Pageable pageable) {
        return groupMemberRepository.findAllByGroup(group, pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMember> getByUser(User user) {
        return groupMemberRepository.findAllByUser(user);
    }

    @Override
    public Long save(User user, Group group) {
        GroupMember groupMember = GroupMember.builder()
                .user(user)
                .group(group)
                .joinedAt(LocalDateTime.now())
                .role(ADMIN.getValue())
                .build();
        return groupMemberRepository.save(groupMember).getId();
    }

    @Override
    public Long saveNoAdmin(User joinedUser, Group group) {
        GroupMember groupMember = GroupMember.builder()
                .user(joinedUser)
                .group(group)
                .joinedAt(LocalDateTime.now())
                .role(MEMBER.getValue())
                .build();
        return groupMemberRepository.save(groupMember).getId();
    }

    private GroupMember requireByUserAndGroup(User user, Group group) {
        return groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() -> new GroupMemberNotFoundException("Участник группы с таким пользователем и группой не найден"));
    }
}
