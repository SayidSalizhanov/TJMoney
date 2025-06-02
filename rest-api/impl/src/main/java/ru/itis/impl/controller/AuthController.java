package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itis.api.AuthApi;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.dto.request.user.UserRegisterRequest;
import ru.itis.impl.model.User;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.RegistrationService;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    private final AuthService authService;
    private final RegistrationService registrationService;

    @Override
    public ResponseEntity<?> login(UserLoginRequest request) {
        return authService.login(request);
    }

    @Override
    public Long register(UserRegisterRequest request) {
        return registrationService.register(request);
    }

    @Override
    public void logout(Long userId) {
        authService.logout();
    }

    @GetMapping("/user")
    public User getUser() {
        return authService.getAuthenticatedUser();
    }
}