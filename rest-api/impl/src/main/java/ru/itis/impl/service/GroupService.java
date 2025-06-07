package ru.itis.impl.service;

import ru.itis.dto.request.application.ApplicationAnswerRequest;
import ru.itis.dto.request.group.GroupCreateRequest;
import ru.itis.dto.request.group.GroupSettingsRequest;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.dto.response.group.*;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

import java.util.List;

public interface GroupService {
    GroupProfileResponse getById(Long groupId, String period);
    List<GroupListResponse> getWhereUserNotJoined(Integer page, Integer amountPerPage);
    void deleteGroupMember(Long id, Long userId);
    GroupSettingsResponse getSettings(Long id);
    void update(Long id, GroupSettingsRequest request);
    void delete(Long id);
    GroupViewingResponse getView(Long id);
    Long create(GroupCreateRequest request);
    List<GroupMemberResponse> getMembers(Long id, Integer page, Integer amountPerPage);
    void deleteGroupMemberFromAdminSide(Long id, Long userIdForDelete);
    List<ApplicationWithUserInfoResponse> getApplications(Long id, Integer page, Integer amountPerPage);
    Long answerApplication(Long id, ApplicationAnswerRequest request);
    void checkUserIsGroupAdmin(User user, Group group);
    void checkUserIsGroupMemberVoid(User user, Group group);
    boolean checkUserIsGroupAdminBoolean(User user, Group group);
}
