package ru.itis.impl.controller;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.api.UserApi;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;
import ru.itis.impl.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public UserSettingsResponse getUserSettingsInfo(Long id, Long userId) {
        return userService.getInfo(id, userId);
    }

    @Override
    public void updateUserSettingsInfo(Long id, Long userId, UserSettingsRequest request) {
        userService.updateInfo(id, userId, request);
    }

    @Override
    public void deleteUser(Long id, Long userId) {
        userService.delete(id, userId);
    }

    @Override
    public List<UserGroupResponse> getUserGroups(Long id, Long userId) {
        return userService.getGroups(id, userId);
    }

    @Override
    public List<ApplicationToGroupResponse> getUserApplicationsToGroup(Long id, Long userId) {
        return userService.getApplications(id, userId);
    }

    @Override
    public void deleteUserApplicationToGroup(Long id, Long userId, Long applicationId) {
        userService.deleteApplication(id, userId, applicationId);
    }

    @Override
    public void changeUserPassword(Long id, Long userId, UserPasswordChangeRequest request) {
        userService.changePassword(id, userId, request);
    }

    @Override
    public String getUserAvatarUrl(Long id, Long userId) {
        return userService.getAvatarUrl(id, userId);
    }

    @Override
    public void changeUserAvatarUrl(Long id, Long userId, MultipartFile avatarImage) {
        userService.changeAvatar(id, userId, avatarImage);
    }

    @Override
    public void deleteUserAvatar(Long id, Long userId) {
        userService.deleteAvatar(id, userId);
    }

    @Override
    public UserProfileResponse getUserProfileInfo(Long id, Long userId, String period) {
        return userService.getProfileInfo(id, userId, period);
    }
}
