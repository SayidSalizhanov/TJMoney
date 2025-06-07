package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.dto.request.user.UserRegisterRequest;

@RequestMapping("/api/auth")
public interface AuthApi {

    @PostMapping("/login")
    ResponseEntity<?> login(
            @RequestBody UserLoginRequest request
    );

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Long register(
            @RequestBody UserRegisterRequest request
    );

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    void logout();
}
