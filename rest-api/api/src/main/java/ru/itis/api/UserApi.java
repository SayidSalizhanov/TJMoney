package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.avatar.AvatarResponse;
import ru.itis.dto.response.user.CheckAdminStatusResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;

import java.util.List;

@RequestMapping("/api/user")
public interface UserApi {

    @GetMapping("/settings")
    @ResponseStatus(HttpStatus.OK)
    UserSettingsResponse getUserSettingsInfo();

    @PutMapping("/settings")
    @ResponseStatus(HttpStatus.OK)
    void updateUserSettingsInfo(
            @RequestBody UserSettingsRequest request
    );

    @DeleteMapping("/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser();

    @GetMapping("/groups")
    List<UserGroupResponse> getUserGroups();

    @GetMapping("/applications")
    @ResponseStatus(HttpStatus.OK)
    List<ApplicationToGroupResponse> getUserApplicationsToGroup();

    @DeleteMapping("/applications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserApplicationToGroup(
            @RequestParam Long applicationId
    );

    @PutMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    void changeUserPassword(
            @RequestBody UserPasswordChangeRequest request
    );

    @GetMapping("/changeAvatar")
    @ResponseStatus(HttpStatus.OK)
    AvatarResponse getUserAvatarUrl();

    @PutMapping("/changeAvatar")
    @ResponseStatus(HttpStatus.OK)
    void changeUserAvatarUrl(
            @RequestBody MultipartFile avatarImage
    );

    @DeleteMapping("/changeAvatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserAvatar();

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    UserProfileResponse getUserProfileInfo(
            @RequestParam(value = "period", required = false, defaultValue = "allTime") String period
    );

    @GetMapping("/role/admin")
    @ResponseStatus(HttpStatus.OK)
    CheckAdminStatusResponse checkAdminRole();
}
