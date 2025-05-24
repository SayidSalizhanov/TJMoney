package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.user.UserSettingsResponse;
import ru.itis.impl.model.User;

@Component
public class UserMapper {

    public UserSettingsResponse toUserSettingsResponse(User user) {
        return UserSettingsResponse.builder()
                .username(user.getUsername())
                .telegramId(user.getTelegramId())
                .sendingToEmail(user.getSendingToEmail())
                .sendingToTelegram(user.getSendingToTelegram())
                .build();
    }
}