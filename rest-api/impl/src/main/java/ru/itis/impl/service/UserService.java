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
    UserSettingsResponse getInfo();
    void updateInfo(UserSettingsRequest request);
    void delete();
    List<UserGroupResponse> getGroups();
    List<ApplicationToGroupResponse> getApplications();
    void deleteApplication(Long applicationId);
    void changePassword(UserPasswordChangeRequest request);
    AvatarResponse getAvatarUrl();
    void changeAvatar(MultipartFile avatarImage);
    void deleteAvatar();
    UserProfileResponse getProfileInfo(String period);
    UserProfileResponse getStrangerProfileInfo(Long id, String period);
}
