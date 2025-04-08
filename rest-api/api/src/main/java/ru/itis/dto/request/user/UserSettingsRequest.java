package ru.itis.dto.request.user;

import lombok.Builder;

@Builder
public record UserSettingsRequest (
        String username,
        String telegramId,
        Boolean sendingToTelegram,
        Boolean sendingToEmail
) {
}
