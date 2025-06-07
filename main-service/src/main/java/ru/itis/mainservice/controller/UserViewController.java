package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.mainservice.dto.response.user.UserProfileResponse;
import ru.itis.mainservice.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable Long id, @RequestParam(required = false) String period, Model model) {
        UserProfileResponse profile = userService.getStrangerUserProfileInfo(id, period);
        model.addAttribute("profile", profile);
        if (profile.transactionsGenerals() == null) model.addAttribute("currentSessionUserId", -1L); // чтобы он точно не совпадал с id и не показывались транзакции на фронте
        else model.addAttribute("currentSessionUserId", profile.id());
        return "user/profile";
    }
}
