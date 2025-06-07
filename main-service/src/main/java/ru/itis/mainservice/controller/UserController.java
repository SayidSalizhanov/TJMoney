package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mainservice.dto.request.user.UserPasswordChangeRequest;
import ru.itis.mainservice.dto.request.user.UserSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationToGroupResponse;
import ru.itis.mainservice.dto.response.exception.ExceptionMessage;
import ru.itis.mainservice.dto.response.user.UserGroupResponse;
import ru.itis.mainservice.dto.response.user.UserProfileResponse;
import ru.itis.mainservice.dto.response.user.UserSettingsResponse;
import ru.itis.mainservice.service.UserService;

import java.util.LinkedHashMap;
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
    public String updateUserSettings(@Valid UserSettingsRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");

            UserSettingsResponse settings = userService.getUserSettingsInfo();
            model.addAttribute("settings", settings);
            model.addAttribute("error", errorMessage);
            return "user/settings";
        }
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
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<UserGroupResponse> groups = userService.getUserGroups(page, amountPerPage);
        model.addAttribute("groups", groups);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);
        return "user/groups";
    }

    @GetMapping("/applications")
    public String getUserApplicationsToGroup(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model) {
        List<ApplicationToGroupResponse> applications = userService.getUserApplicationsToGroup(page, amountPerPage);
        model.addAttribute("applications", applications);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);
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
    public String changeUserPassword(@Valid UserPasswordChangeRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            return "user/changePassword";
        }

        ResponseEntity<?> response = userService.changeUserPassword(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            return "redirect:/user/settings";
        }

        Object responseBody = response.getBody();
        if (responseBody instanceof LinkedHashMap hashMap) {
            model.addAttribute("error", hashMap.get("message"));
        } else {
            model.addAttribute("error", "Произошла ошибка при смене пароля");
        }
        return "user/changePassword";
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