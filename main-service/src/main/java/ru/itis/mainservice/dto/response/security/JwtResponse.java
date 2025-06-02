package ru.itis.mainservice.dto.response.security;

import lombok.Builder;

@Builder
public record JwtResponse(String token) {
}
