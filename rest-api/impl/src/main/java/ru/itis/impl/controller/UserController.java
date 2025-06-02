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
    public UserSettingsResponse getUserSettingsInfo() {
        return userService.getInfo();
    }

    @Override
    public void updateUserSettingsInfo(UserSettingsRequest request) {
        userService.updateInfo(request);
    }

    @Override
    public void deleteUser() {
        userService.delete();
    }

    @Override
    public List<UserGroupResponse> getUserGroups() {
        return userService.getGroups();
    }

    @Override
    public List<ApplicationToGroupResponse> getUserApplicationsToGroup() {
        return userService.getApplications();
    }

    @Override
    public void deleteUserApplicationToGroup(Long applicationId) {
        userService.deleteApplication(applicationId);
    }

    @Override
    public void changeUserPassword(UserPasswordChangeRequest request) {
        userService.changePassword(request);
    }

    @Override
    public AvatarResponse getUserAvatarUrl() {
        return userService.getAvatarUrl();
    }

    @Override
    public void changeUserAvatarUrl(MultipartFile avatarImage) {
        userService.changeAvatar(avatarImage);
    }

    @Override
    public void deleteUserAvatar() {
        userService.deleteAvatar();
    }

    @Override
    public UserProfileResponse getUserProfileInfo(String period) {
        return userService.getProfileInfo(period);
    }
}
