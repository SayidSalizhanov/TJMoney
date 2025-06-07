package ru.itis.impl.service;

import org.springframework.http.ResponseEntity;
import ru.itis.dto.request.user.UserLoginRequest;
import ru.itis.dto.response.user.CheckAdminStatusResponse;

public interface AuthService {
    ResponseEntity<?> login(UserLoginRequest request);
    void logout();
    Long getAuthenticatedUserId();
    CheckAdminStatusResponse authSercheckAdminRole();
}
