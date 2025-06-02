package ru.itis.impl.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.impl.model.User;

public interface AuthService {
    ResponseEntity<?> login(UserLoginRequest request);
    void logout();
    User getAuthenticatedUser();
}
