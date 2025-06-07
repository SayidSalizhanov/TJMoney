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
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.GroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController implements GroupApi {

    private final GroupService groupService;
    private final AuthService authService;
    private final ApplicationService applicationService;

    @Override
    public List<GroupListResponse> getGroupsWhereUserNotJoined(Integer page, Integer amountPerPage) {
        return groupService.getWhereUserNotJoined(page, amountPerPage);
    }

    @Override
    public Long createApplicationToGroup(Long groupId) {
        return applicationService.createApplications(groupId);
    }

    @Override
    public GroupProfileResponse getGroup(Long id, String period) {
        return groupService.getById(id, period);
    }

    @Override
    public void leaveGroup(Long id) {
        groupService.deleteGroupMember(id, authService.getAuthenticatedUserId());
    }

    @Override
    public GroupSettingsResponse getGroupSettings(Long id) {
        return groupService.getSettings(id);
    }

    @Override
    public void updateGroupInfo(Long id, GroupSettingsRequest request) {
        groupService.update(id, request);
    }

    @Override
    public void deleteGroup(Long id) {
        groupService.delete(id);
    }

    @Override
    public GroupViewingResponse getGroupView(Long id) {
        return groupService.getView(id);
    }

    @Override
    public Long createGroup(GroupCreateRequest request) {
        return groupService.create(request);
    }

    @Override
    public List<GroupMemberResponse> getGroupMembers(Long id, Integer page, Integer amountPerPage) {
        return groupService.getMembers(id, page, amountPerPage);
    }

    @Override
    public void deleteMemberFromAdminSide(Long id, Long userIdForDelete) {
        groupService.deleteGroupMemberFromAdminSide(id, userIdForDelete);
    }

    @Override
    public List<ApplicationWithUserInfoResponse> getApplications(Long id, Integer page, Integer amountPerPage) {
        return groupService.getApplications(id, page, amountPerPage);
    }

    @Override
    public Long answerApplication(Long id, ApplicationAnswerRequest request) {
        return groupService.answerApplication(id, request);
    }
}
