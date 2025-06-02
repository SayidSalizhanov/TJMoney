package ru.itis.dto.response.fxrates;

import lombok.Builder;

import java.util.Map;

@Builder
public record FxRatesResponse (
        Boolean success,
        String timestamp,
        String base,
        Map<String, Double> rates
) {
}