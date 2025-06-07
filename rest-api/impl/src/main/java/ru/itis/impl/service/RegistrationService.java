package ru.itis.impl.service;

import ru.itis.dto.request.user.UserRegisterRequest;

public interface RegistrationService {
    Long register(UserRegisterRequest request);
}
