package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.dto.request.user.UserRegisterRequest;

public interface AuthApi {

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    Long login(
            @RequestBody UserLoginRequest request
    );

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    Long register(
            @RequestBody UserRegisterRequest request
    );

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    void logout(
            @RequestParam("userId") Long userId // todo get from authentication
    );
}
