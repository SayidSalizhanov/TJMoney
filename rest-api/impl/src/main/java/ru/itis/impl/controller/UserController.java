package ru.itis.impl.controller;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.api.UserApi;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.avatar.AvatarResponse;
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
    public UserSettingsResponse getUserSettingsInfo(Long id) {
        return userService.getInfo(id);
    }

    @Override
    public void updateUserSettingsInfo(Long id, UserSettingsRequest request) {
        userService.updateInfo(id, request);
    }

    @Override
    public void deleteUser(Long id) {
        userService.delete(id);
    }

    @Override
    public List<UserGroupResponse> getUserGroups(Long id) {
        return userService.getGroups(id);
    }

    @Override
    public List<ApplicationToGroupResponse> getUserApplicationsToGroup(Long id) {
        return userService.getApplications(id);
    }

    @Override
    public void deleteUserApplicationToGroup(Long id, Long applicationId) {
        userService.deleteApplication(id, applicationId);
    }

    @Override
    public void changeUserPassword(Long id, UserPasswordChangeRequest request) {
        userService.changePassword(id, request);
    }

    @Override
    public AvatarResponse getUserAvatarUrl(Long id) {
        return userService.getAvatarUrl(id);
    }

    @Override
    public void changeUserAvatarUrl(Long id, MultipartFile avatarImage) {
        userService.changeAvatar(id, avatarImage);
    }

    @Override
    public void deleteUserAvatar(Long id) {
        userService.deleteAvatar(id);
    }

    @Override
    public UserProfileResponse getUserProfileInfo(Long id, String period) {
        return userService.getProfileInfo(id, period);
    }
}
