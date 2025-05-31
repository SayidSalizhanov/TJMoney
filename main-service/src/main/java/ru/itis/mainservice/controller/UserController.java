package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mainservice.dto.request.user.UserPasswordChangeRequest;
import ru.itis.mainservice.dto.request.user.UserSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationToGroupResponse;
import ru.itis.mainservice.dto.response.user.UserGroupResponse;
import ru.itis.mainservice.dto.response.user.UserProfileResponse;
import ru.itis.mainservice.dto.response.user.UserSettingsResponse;
import ru.itis.mainservice.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}/settings")
    public String getUserSettings(@PathVariable Long id, @RequestParam Long userId, Model model) {
        UserSettingsResponse settings = userService.getUserSettingsInfo(id, userId);
        model.addAttribute("settings", settings);
        return "user/settings";
    }

    @PutMapping("/{id}/settings")
    public void updateUserSettings(@PathVariable Long id, @RequestParam Long userId, @RequestBody UserSettingsRequest request) {
        userService.updateUserSettingsInfo(id, userId, request);
    }

    @DeleteMapping("/{id}/settings")
    public void deleteUser(@PathVariable Long id, @RequestParam Long userId) {
        userService.deleteUser(id, userId);
    }

    @GetMapping("/{id}/groups")
    public String getUserGroups(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<UserGroupResponse> groups = userService.getUserGroups(id, userId, page, amountPerPage);
        model.addAttribute("groups", groups);
        return "user/groups";
    }

    @GetMapping("/{id}/applications")
    public String getUserApplications(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<ApplicationToGroupResponse> applications = userService.getUserApplicationsToGroup(id, userId, page, amountPerPage);
        model.addAttribute("applications", applications);
        return "user/applications";
    }

    @DeleteMapping("/{id}/applications")
    public void deleteUserApplication(@PathVariable Long id, @RequestParam Long userId, @RequestParam Long applicationId) {
        userService.deleteUserApplicationToGroup(id, userId, applicationId);
    }

    @PatchMapping("/{id}/changePassword")
    public void changeUserPassword(@PathVariable Long id, @RequestParam Long userId, @RequestBody UserPasswordChangeRequest request) {
        userService.changeUserPassword(id, userId, request);
    }

    @GetMapping("/{id}/changeAvatar")
    @ResponseBody
    public String getUserAvatarUrl(@PathVariable Long id, @RequestParam Long userId) {
        return userService.getUserAvatarUrl(id, userId);
    }

    @PatchMapping("/{id}/changeAvatar")
    @ResponseBody
    public void changeUserAvatar(@PathVariable Long id, @RequestParam Long userId, @RequestParam MultipartFile avatarImage) {
        userService.changeUserAvatarUrl(id, userId, avatarImage);
    }

    @DeleteMapping("/{id}/changeAvatar")
    @ResponseBody
    public void deleteUserAvatar(@PathVariable Long id, @RequestParam Long userId) {
        userService.deleteUserAvatar(id, userId);
    }

    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable Long id, @RequestParam Long userId, @RequestParam(required = false) String period, Model model) {
        UserProfileResponse profile = userService.getUserProfileInfo(id, userId, period);
        model.addAttribute("profile", profile);
        model.addAttribute("currentSessionUserId", userId);
        return "user/profile";
    }
} 