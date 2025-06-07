package ru.itis.dto.response.fxrates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record FxRatesResponse (
        Boolean success,
        String timestamp,
        String base,
        Map<String, Double> rates
) {
}