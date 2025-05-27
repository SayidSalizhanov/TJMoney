package ru.itis.api;

import jakarta.servlet.http.Part;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;

import java.util.List;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    UserSettingsResponse getUserSettingsInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    void updateUserSettingsInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody UserSettingsRequest request
    );

    @DeleteMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping("/{id}/groups")
    List<UserGroupResponse> getUserGroups(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "groupName") String sort
    );

    @GetMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    List<ApplicationToGroupResponse> getUserApplicationsToGroup(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "groupName") String sort
    );

    @DeleteMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserApplicationToGroup(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody Long applicationId
    );

    @PatchMapping("/{id}/changePassword")
    @ResponseStatus(HttpStatus.OK)
    void changeUserPassword(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody UserPasswordChangeRequest request
    );

    @GetMapping("/{id}/changeAvatar")
    @ResponseStatus(HttpStatus.OK)
    String getUserAvatarUrl(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PatchMapping("/{id}/changeAvatar")
    @ResponseStatus(HttpStatus.OK)
    void changeUserAvatarUrl(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody MultipartFile avatarImage
    );

    @DeleteMapping("/{id}/changeAvatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserAvatar(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserProfileResponse getUserProfileInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "period", required = false, defaultValue = "allTime") String period
    );
}
