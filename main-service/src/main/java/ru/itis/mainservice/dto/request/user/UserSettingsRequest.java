package ru.itis.mainservice.dto.request.user;

import lombok.Builder;

@Builder
public record UserSettingsRequest (
        String username,
        String telegramId,
        boolean sendingToTelegram,
        boolean sendingToEmail
) {
}
