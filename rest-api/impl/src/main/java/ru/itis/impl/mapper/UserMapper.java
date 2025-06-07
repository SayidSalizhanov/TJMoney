package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;
import ru.itis.impl.model.User;

import java.util.List;
import java.util.Map;

@Component
public class UserMapper {

    public UserSettingsResponse toUserSettingsResponse(User user) {
        return UserSettingsResponse.builder()
                .username(user.getUsername())
                .telegramId(user.getTelegramId())
                .sendingToEmail(user.getSendingToEmail())
                .build();
    }

    public UserProfileResponse toUserProfileResponse(User user, List<Map<String, Integer>> transactionsGenerals) {
        return UserProfileResponse.builder()
                .transactionsGenerals(transactionsGenerals)
                .id(user.getId())
                .username(user.getUsername())
                .telegramId(user.getTelegramId())
                .email(user.getEmail())
                .urlAvatar(user.getAvatar().getUrl())
                .build();
    }
}