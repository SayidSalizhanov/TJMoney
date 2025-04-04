package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.itis.dto.request.user.UserRegisterRequest;

@RequestMapping("/register")
public interface RegisterApi {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long register(
            @RequestBody UserRegisterRequest request
    );
}
