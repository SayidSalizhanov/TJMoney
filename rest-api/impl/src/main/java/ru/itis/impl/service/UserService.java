package ru.itis.impl.service;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;

import java.util.List;

public interface UserService {
    UserSettingsResponse getInfo(Long id, Long userId);
    void updateInfo(Long id, Long userId, UserSettingsRequest request);
    void delete(Long id, Long userId);
    List<UserGroupResponse> getGroups(Long id, Long userId, Integer page, Integer amountPerPage, String sort);
    List<ApplicationToGroupResponse> getApplications(Long id, Long userId, Integer page, Integer amountPerPage, String sort);
    void deleteApplication(Long id, Long userId, Long applicationId);
    void changePassword(Long id, Long userId, UserPasswordChangeRequest request);
    String getAvatarUrl(Long id, Long userId);
    void changeAvatar(Long id, Long userId, MultipartFile avatarImage);
    void deleteAvatar(Long id, Long userId);
    UserProfileResponse getProfileInfo(Long id, Long userId, String period);
}
