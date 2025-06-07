package ru.itis.dto.response.user;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record UserProfileResponse (
        List<Map<String, Integer>> transactionsGenerals,
        Long id,
        String username,
        String email,
        String telegramId,
        String urlAvatar
) {
}
