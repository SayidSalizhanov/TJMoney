package ru.itis.impl.service;

import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.user.UserSettingsResponse;

public interface UserService {

    UserSettingsResponse getUserSettingsInfo(Long id);

    void updateUserSettingsInfo(Long id, UserSettingsRequest request);

    void deleteUser(Long id);

    String getUserAvatarUrl(Long id);
}
