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
    GroupProfileResponse getById(Long groupId, Long userId, String period);
    List<GroupListResponse> getWhereUserNotJoined(Long userId);
    void deleteGroupMember(Long id, Long userId);
    GroupSettingsResponse getSettings(Long id, Long userId);
    void update(Long id, Long userId, GroupSettingsRequest request);
    void delete(Long id, Long userId);
    GroupViewingResponse getView(Long id, Long userId);
    Long create(Long id, Long userId, GroupCreateRequest request);
    List<GroupMemberResponse> getMembers(Long id, Long userId, Integer page, Integer amountPerPage, String sort);
    void deleteGroupMemberFromAdminSide(Long id, Long userId, Long userIdForDelete);
    List<ApplicationWithUserInfoResponse> getApplications(Long id, Long userId, Integer page, Integer amountPerPage, String sort);
    Long answerApplication(Long id, Long userId, ApplicationAnswerRequest request);
    void checkUserIsGroupAdmin(User user, Group group);
    void checkUserIsGroupMemberVoid(User user, Group group);
    boolean checkUserIsGroupAdminBoolean(User user, Group group);
}
