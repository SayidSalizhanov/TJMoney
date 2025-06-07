package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
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
import ru.itis.impl.service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.itis.impl.enums.ApplicationStatusEnum.*;
import static ru.itis.impl.enums.GroupMemberStatusEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    private final GroupMemberRepository groupMemberRepository;
    private final ApplicationRepository applicationRepository;

    private final TransactionService transactionService;
    private final GroupMemberService groupMemberService;
    private final ApplicationService applicationService;
    private final GroupMemberMapper groupMemberMapper;
    private final ApplicationMapper applicationMapper;
    private final AuthService authService;

    private final UserGroupRequireService userGroupRequireService;

    @Override
    @Transactional(readOnly = true)
    public GroupProfileResponse getById(Long groupId, @MayBeNull String period) {
        Group group = userGroupRequireService.requireGroupById(groupId);
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        checkUserIsGroupMemberVoid(user, group);

        String userRole = groupMemberService.getGroupMember(user, group).getRole();
        List<Map<String, Integer>> transactionsGenerals;
        if (period == null || period.isEmpty()) transactionsGenerals = transactionService.getGroupTransactionsGenerals(groupId, "all");
        else transactionsGenerals = transactionService.getGroupTransactionsGenerals(groupId, period);

        return groupMapper.toGroupProfileResponse(transactionsGenerals, userRole, group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupListResponse> getWhereUserNotJoined(Integer page, Integer amountPerPage) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        List<GroupMember> groupMembers = groupMemberRepository.findAllByUser(user);
        List<Long> userGroups = groupMembers.stream()
                .map(gm -> gm.getGroup().getId())
                .toList();
        List<Long> nonRejectedUserApplicationsGroupIds = applicationRepository.findByUser(user).stream()
                .filter(a -> !a.getStatus().equalsIgnoreCase(REJECTED.getValue()))
                .map(a -> a.getGroup().getId())
                .toList();

        List<Long> excludedIds = new ArrayList<>(userGroups.size() + nonRejectedUserApplicationsGroupIds.size());
        excludedIds.addAll(userGroups);
        excludedIds.addAll(nonRejectedUserApplicationsGroupIds);

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("name"));

        if (excludedIds.isEmpty()) return groupRepository.findAll(pageable).stream()
                .map(groupMapper::toGroupListResponse)
                .toList();

        return groupRepository.findByIdNotIn(excludedIds, pageable).stream()
                .map(groupMapper::toGroupListResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGroupMember(Long id, Long userId) {
        Group group = userGroupRequireService.requireGroupById(id);
        User user = userGroupRequireService.requireUserById(userId);

        checkUserIsGroupMemberVoid(user, group);

        groupMemberRepository.deleteByGroupAndUser(group, user);
        applicationRepository.deleteByGroupAndUser(group, user);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupSettingsResponse getSettings(Long id) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        String userRole = groupMemberService.getGroupMember(user, group).getRole();

        return groupMapper.toGroupSettingsResponse(group, userRole);
    }

    @Override
    @Transactional
    public void update(Long id, GroupSettingsRequest request) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        group.setName(request.name());
        group.setDescription(request.description());
        groupRepository.save(group);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        groupRepository.delete(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupViewingResponse getView(Long id) {
        Group group = userGroupRequireService.requireGroupById(id);

        List<GroupMember> groupMembers = groupMemberService.getGroupMembers(group);

        Integer membersCount = groupMembers.size();

        String adminUsername = "fake-admin";
        for (GroupMember groupMember : groupMembers) {
            if (groupMember.getRole().equalsIgnoreCase(ADMIN.getValue())) {
                adminUsername = groupMember.getUser().getUsername();
                break;
            }
        }

        return groupMapper.toGroupViewingResponse(group, membersCount, adminUsername);
    }

    @Override
    @Transactional
    public Long create(GroupCreateRequest request) {
        Group newGroup = Group.builder()
                .name(request.name())
                .createdAt(LocalDateTime.now())
                .description(request.description())
                .build();
        Group group = groupRepository.save(newGroup);

        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        groupMemberService.save(user, group);

        return group.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupMemberResponse> getMembers(Long id, Integer page, Integer amountPerPage) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupMemberVoid(user, group);

        Pageable pageable = PageRequest.of(page, amountPerPage);

        return groupMemberService.getGroupMembersWithPagination(group, pageable).stream()
                .map(groupMemberMapper::toGroupMemberResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteGroupMemberFromAdminSide(Long id, Long userIdForDelete) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);
        deleteGroupMember(id, userIdForDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationWithUserInfoResponse> getApplications(Long id, Integer page, Integer amountPerPage) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("sendAt"));

        List<Application> applications = applicationService.getGroupApplicationsByGroupIdAndByStatus(id, PENDING.getValue(), pageable);
        return applications.stream()
                .map(applicationMapper::toApplicationWithUserInfoResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long answerApplication(Long id, ApplicationAnswerRequest request) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        Group group = userGroupRequireService.requireGroupById(id);

        checkUserIsGroupAdmin(user, group);

        applicationService.updateStatus(request.applicationId(), request.applicationStatus());
        if (request.applicationStatus().equalsIgnoreCase(APPROVED.getValue())) {
            User joinedUser = userGroupRequireService.requireUserById(request.userForJoinId());
            groupMemberService.saveNoAdmin(joinedUser, group);
        }

        return request.applicationId();
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
        if (!groupMember.getRole().equalsIgnoreCase(ADMIN.getValue())) throw new AccessDeniedException("Чтобы иметь доступ к настройкам группы, нужно обладать правами админа группы");
    }

    @Override
    public boolean checkUserIsGroupAdminBoolean(User user, Group group) {
        GroupMember groupMember = checkUserIsGroupMember(user, group);
        return groupMember.getRole().equalsIgnoreCase(ADMIN.getValue());
    }
}
