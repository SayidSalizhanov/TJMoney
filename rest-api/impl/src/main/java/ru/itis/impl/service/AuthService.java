package ru.itis.impl.service;

import org.springframework.http.ResponseEntity;
import ru.itis.dto.request.user.UserLoginRequest;

public interface AuthService {
    ResponseEntity<?> login(UserLoginRequest request);
    void logout();
    Long getAuthenticatedUserId();
}
