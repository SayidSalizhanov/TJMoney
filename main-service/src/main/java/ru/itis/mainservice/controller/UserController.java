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
import ru.itis.mainservice.service.AuthService;
import ru.itis.mainservice.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/{id}/settings")
    public String getUserSettings(@PathVariable Long id, Model model) {
        UserSettingsResponse settings = userService.getUserSettingsInfo(id);
        model.addAttribute("settings", settings);
        model.addAttribute("userId", id);
        return "user/settings";
    }

    @PutMapping("/{id}/settings")
    public String updateUserSettings(@PathVariable Long id, UserSettingsRequest request) {
        userService.updateUserSettingsInfo(id, request);
        return "redirect:/users/" + id;
    }

    @DeleteMapping("/{id}/settings")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/articles";
    }

    @GetMapping("/{id}/groups")
    public String getUserGroups(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<UserGroupResponse> groups = userService.getUserGroups(id, page, amountPerPage);
        model.addAttribute("groups", groups);
        model.addAttribute("userId", id);
        return "user/groups";
    }

    @GetMapping("/{id}/applications")
    public String getUserApplications(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<ApplicationToGroupResponse> applications = userService.getUserApplicationsToGroup(id, page, amountPerPage);
        model.addAttribute("applications", applications);
        model.addAttribute("userId", id);
        return "user/applications";
    }

    @DeleteMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserApplication(@PathVariable Long id, @RequestParam Long applicationId) {
        userService.deleteUserApplicationToGroup(id, applicationId);
    }

    @GetMapping("/{id}/changePassword")
    public String getChangePasswordPage(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        return "user/changePassword";
    }

    @PatchMapping("/{id}/changePassword")
    public String changeUserPassword(@PathVariable Long id, UserPasswordChangeRequest request) {
        userService.changeUserPassword(id, request);
        return "redirect:/users/" + id + "/changePassword";
    }

    @GetMapping("/{id}/changeAvatar")
    public String getUserAvatarUrl(@PathVariable Long id, Model model) {
        model.addAttribute("urlPhoto", userService.getUserAvatarUrl(id));
        model.addAttribute("userId", id);
        return "user/changeAvatar";
    }

    @PatchMapping("/{id}/changeAvatar")
    public String changeUserAvatar(@PathVariable Long id, @RequestParam MultipartFile avatarImage) {
        userService.changeUserAvatarUrl(id, avatarImage);
        return "redirect:/users/" + id;
    }

    @DeleteMapping("/{id}/changeAvatar")
    public String deleteUserAvatar(@PathVariable Long id) {
        userService.deleteUserAvatar(id);
        return "redirect:/users/" + id;
    }

    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable Long id, @RequestParam(required = false) String period, Model model) {
        UserProfileResponse profile = userService.getUserProfileInfo(id, period);
        model.addAttribute("profile", profile);
        model.addAttribute("currentSessionUserId", authService.getAuthenticatedUserId());
        return "user/profile";
    }
} 