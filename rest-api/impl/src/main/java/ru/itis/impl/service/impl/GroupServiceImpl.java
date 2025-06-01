package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.application.ApplicationAnswerRequest;
import ru.itis.dto.request.group.GroupCreateRequest;
import ru.itis.dto.request.group.GroupSettingsRequest;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.dto.response.group.*;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.AccessDeniedException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.ApplicationMapper;
import ru.itis.impl.mapper.GroupMapper;
import ru.itis.impl.mapper.GroupMemberMapper;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.ApplicationRepository;
import ru.itis.impl.repository.GroupMemberRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.ApplicationService;
import ru.itis.impl.service.GroupMemberService;
import ru.itis.impl.service.GroupService;
import ru.itis.impl.service.TransactionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    private final TransactionService transactionService;
    private final GroupMemberService groupMemberService;
    private final ApplicationService applicationService;
    private final GroupMemberMapper groupMemberMapper;
    private final ApplicationMapper applicationMapper;

    @Override
    @Transactional(readOnly = true)
    public GroupProfileResponse getById(Long groupId, Long userId, @MayBeNull String period) {
        Group group = requireGroupById(groupId);
        User user = requireUserById(userId);

        checkUserIsGroupMemberVoid(user, group);

        String userRole = groupMemberService.getGroupMember(user, group).getRole();
        List<Map<String, Integer>> transactionsGenerals;
        if (period == null || period.isEmpty()) transactionsGenerals = transactionService.getGroupTransactionsGenerals(groupId, "all");
        else transactionsGenerals = transactionService.getGroupTransactionsGenerals(groupId, period);

        return groupMapper.toGroupProfileResponse(transactionsGenerals, userRole, group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupListResponse> getWhereUserNotJoined(Long userId) {
        User user = requireUserById(userId);
        List<GroupMember> groupMembers = groupMemberRepository.findAllByUser(user);
        List<Long> userGroups = groupMembers.stream()
                .map(gm -> gm.getGroup().getId())
                .toList();
        List<Long> nonRejectedUserApplicationsGroupIds = applicationRepository.findByUser(user).stream()
                .filter(a -> !a.getStatus().equalsIgnoreCase("Отклонено"))
                .map(a -> a.getGroup().getId())
                .toList();

        List<Long> excludedIds = new ArrayList<>(userGroups.size() + nonRejectedUserApplicationsGroupIds.size());
        excludedIds.addAll(userGroups);
        excludedIds.addAll(nonRejectedUserApplicationsGroupIds);

        if (excludedIds.isEmpty()) return groupRepository.findAll().stream()
                .map(groupMapper::toGroupListResponse)
                .toList();

        return groupRepository.findByIdNotIn(excludedIds).stream()
                .map(groupMapper::toGroupListResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGroupMember(Long id, Long userId) {
        Group group = requireGroupById(id);
        User user = requireUserById(userId);

        checkUserIsGroupMemberVoid(user, group);

        groupMemberRepository.deleteByGroupAndUser(group, user);
        applicationRepository.deleteByGroupAndUser(group, user);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupSettingsResponse getSettings(Long id, Long userId) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        String userRole = groupMemberService.getGroupMember(user, group).getRole();

        return groupMapper.toGroupSettingsResponse(group, userRole);
    }

    @Override
    @Transactional
    public void update(Long id, Long userId, GroupSettingsRequest request) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        group.setName(request.name());
        group.setDescription(request.description());
        groupRepository.save(group);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        groupRepository.delete(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupViewingResponse getView(Long id, Long userId) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        List<GroupMember> groupMembers = groupMemberService.getGroupMembers(group);

        Integer membersCount = groupMembers.size();

        String adminUsername = "fake-admin";
        for (GroupMember groupMember : groupMembers) {
            if (groupMember.getRole().equalsIgnoreCase("ADMIN")) {
                adminUsername = groupMember.getUser().getUsername();
                break;
            }
        }

        return groupMapper.toGroupViewingResponse(group, membersCount, adminUsername);
    }

    @Override
    @Transactional
    public Long create(Long id, Long userId, GroupCreateRequest request) {
        Group newGroup = Group.builder()
                .name(request.name())
                .createdAt(LocalDateTime.now())
                .description(request.description())
                .build();
        Group group = groupRepository.save(newGroup);

        User user = requireUserById(userId);
        groupMemberService.save(user, group);

        return group.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberResponse> getMembers(Long id, Long userId, Integer page, Integer amountPerPage) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupMemberVoid(user, group);

        Pageable pageable = PageRequest.of(page, amountPerPage);

        return groupMemberService.getGroupMembersWithPagination(group, pageable).stream()
                .map(groupMemberMapper::toGroupMemberResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGroupMemberFromAdminSide(Long id, Long userId, Long userIdForDelete) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);
        deleteGroupMember(id, userIdForDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationWithUserInfoResponse> getApplications(Long id, Long userId, Integer page, Integer amountPerPage) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("sendAt"));

        List<Application> applications = applicationService.getGroupApplicationsByGroupIdAndByStatus(id, "В ожидании", pageable);
        return applications.stream()
                .map(applicationMapper::toApplicationWithUserInfoResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long answerApplication(Long id, Long userId, ApplicationAnswerRequest request) {
        User user = requireUserById(userId);
        Group group = requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        applicationService.updateStatus(request.applicationId(), request.applicationStatus());
        if (request.applicationStatus().equalsIgnoreCase("Одобрено")) {
            User joinedUser = requireUserById(request.userForJoinId());
            groupMemberService.saveNoAdmin(joinedUser, group);
        }

        return request.applicationId();
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }

    @Override
    public void checkUserIsGroupMemberVoid(User user, Group group) {
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByGroupAndUser(group, user);
        if (optionalGroupMember.isEmpty()) throw new AccessDeniedException("Доступ к данным имеют только участники группы");
    }

    private GroupMember checkUserIsGroupMember(User user, Group group) {
        Optional<GroupMember> optionalGroupMember = groupMemberRepository.findByGroupAndUser(group, user);
        if (optionalGroupMember.isEmpty()) throw new AccessDeniedException("Доступ к данным имеют только участники группы");
        return optionalGroupMember.get();
    }

    @Override
    public void checkUserIsGroupAdmin(User user, Group group) {
        GroupMember groupMember = checkUserIsGroupMember(user, group);
        if (!groupMember.getRole().equalsIgnoreCase("ADMIN")) throw new AccessDeniedException("Чтобы иметь доступ к настройкам группы, нужно обладать правами админа группы");
    }

    @Override
    public boolean checkUserIsGroupAdminBoolean(User user, Group group) {
        GroupMember groupMember = checkUserIsGroupMember(user, group);
        return groupMember.getRole().equalsIgnoreCase("ADMIN");
    }
}
