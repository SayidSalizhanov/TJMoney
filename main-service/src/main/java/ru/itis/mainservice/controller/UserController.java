package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/settings")
    public String getUserSettings(Model model) {
        UserSettingsResponse settings = userService.getUserSettingsInfo();
        model.addAttribute("settings", settings);
        return "user/settings";
    }

    @PutMapping("/settings")
    public String updateUserSettings(UserSettingsRequest request) {
        userService.updateUserSettingsInfo(request);
        return "redirect:/user";
    }

    @DeleteMapping("/settings")
    public String deleteUser() {
        userService.deleteUser();
        return "redirect:/articles";
    }

    @GetMapping("/groups")
    public String getUserGroups(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<UserGroupResponse> groups = userService.getUserGroups(page, amountPerPage);
        model.addAttribute("groups", groups);
        return "user/groups";
    }

    @GetMapping("/applications")
    public String getUserApplications(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<ApplicationToGroupResponse> applications = userService.getUserApplicationsToGroup(page, amountPerPage);
        model.addAttribute("applications", applications);
        return "user/applications";
    }

    @DeleteMapping("/applications")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserApplication(@RequestParam Long applicationId) {
        userService.deleteUserApplicationToGroup(applicationId);
    }

    @GetMapping("/changePassword")
    public String getChangePasswordPage(Model model) {
        return "user/changePassword";
    }

    @PatchMapping("/changePassword")
    public String changeUserPassword(UserPasswordChangeRequest request) {
        userService.changeUserPassword(request);
        return "redirect:/user/changePassword";
    }

    @GetMapping("/changeAvatar")
    public String getUserAvatarUrl(Model model) {
        model.addAttribute("urlPhoto", userService.getUserAvatarUrl());
        return "user/changeAvatar";
    }

    @PatchMapping("/changeAvatar")
    public String changeUserAvatar(@RequestParam MultipartFile avatarImage) {
        userService.changeUserAvatarUrl(avatarImage);
        return "redirect:/user/changeAvatar";
    }

    @DeleteMapping("/changeAvatar")
    public String deleteUserAvatar() {
        userService.deleteUserAvatar();
        return "redirect:/user";
    }

    @GetMapping
    public String getUserProfile(@RequestParam(required = false) String period, Model model) {
        UserProfileResponse profile = userService.getUserProfileInfo(period);
        model.addAttribute("profile", profile);
        model.addAttribute("currentSessionUserId", profile.id()); // profile.id() будет равен id текущего аутентифицированного пользователя
        return "user/profile";
    }
} 