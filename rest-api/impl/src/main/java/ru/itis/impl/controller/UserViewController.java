package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.impl.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserViewController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getUserProfileInfo(@PathVariable Long id, @RequestParam(required = false) String period) {
        return userService.getStrangerProfileInfo(id, period);
    }
}
