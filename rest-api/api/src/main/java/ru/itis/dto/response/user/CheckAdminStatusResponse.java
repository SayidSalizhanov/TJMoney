package ru.itis.dto.response.user;

import lombok.Builder;

@Builder
public record CheckAdminStatusResponse(boolean status) {
}
