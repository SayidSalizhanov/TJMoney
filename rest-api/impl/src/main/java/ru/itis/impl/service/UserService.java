package ru.itis.impl.service;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.avatar.AvatarResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;

import java.util.List;

public interface UserService {
    UserSettingsResponse getInfo(Long id);
    void updateInfo(Long id, UserSettingsRequest request);
    void delete(Long id);
    List<UserGroupResponse> getGroups(Long id);
    List<ApplicationToGroupResponse> getApplications(Long id);
    void deleteApplication(Long id, Long applicationId);
    void changePassword(Long id, UserPasswordChangeRequest request);
    AvatarResponse getAvatarUrl(Long id);
    void changeAvatar(Long id, MultipartFile avatarImage);
    void deleteAvatar(Long id);
    UserProfileResponse getProfileInfo(Long id, String period);
}
