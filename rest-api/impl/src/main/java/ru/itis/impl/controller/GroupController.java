package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.GroupApi;
import ru.itis.dto.request.application.ApplicationAnswerRequest;
import ru.itis.dto.request.group.GroupCreateRequest;
import ru.itis.dto.request.group.GroupSettingsRequest;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.dto.response.group.*;
import ru.itis.impl.service.ApplicationService;
import ru.itis.impl.service.GroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController implements GroupApi {

    private final GroupService groupService;
    private final ApplicationService applicationService;

    @Override
    public List<GroupListResponse> getGroupsWhereUserNotJoined(Long userId) {
        return groupService.getWhereUserNotJoined(userId);
    }

    @Override
    public Long createApplicationToGroup(Long userId, Long groupId) {
        return applicationService.createApplications(userId, groupId);
    }

    @Override
    public GroupProfileResponse getGroup(Long id, Long userId, String period) {
        return groupService.getById(id, userId, period);
    }

    @Override
    public void leaveGroup(Long id, Long userId) {
        groupService.deleteGroupMember(id, userId);
    }

    @Override
    public GroupSettingsResponse getGroupSettings(Long id, Long userId) {
        return groupService.getSettings(id, userId);
    }

    @Override
    public void updateGroupInfo(Long id, Long userId, GroupSettingsRequest request) {
        groupService.update(id, userId, request);
    }

    @Override
    public void deleteGroup(Long id, Long userId) {
        groupService.delete(id, userId);
    }

    @Override
    public GroupViewingResponse getGroupView(Long id, Long userId) {
        return groupService.getView(id, userId);
    }

    @Override
    public Long createGroup(Long id, Long userId, GroupCreateRequest request) {
        return groupService.create(id, userId, request);
    }

    @Override
    public List<GroupMemberResponse> getGroupMembers(Long id, Long userId, Integer page, Integer amountPerPage, String sort) {
        return groupService.getMembers(id, userId, page, amountPerPage, sort);
    }

    @Override
    public void deleteMemberFromAdminSide(Long id, Long userId, Long userIdForDelete) {
        groupService.deleteGroupMemberFromAdminSide(id, userId, userIdForDelete);
    }

    @Override
    public List<ApplicationWithUserInfoResponse> getApplications(Long id, Long userId, Integer page, Integer amountPerPage, String sort) {
        return groupService.getApplications(id, userId, page, amountPerPage, sort);
    }

    @Override
    public Long answerApplication(Long id, Long userId, ApplicationAnswerRequest request) {
        return groupService.answerApplication(id, userId, request);
    }
}
