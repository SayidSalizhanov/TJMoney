package ru.itis.mainservice.dto.response.user;

import lombok.Builder;

@Builder
public record UserSettingsResponse (
        String username,
        String telegramId,
        Boolean sendingToEmail
) {
}
