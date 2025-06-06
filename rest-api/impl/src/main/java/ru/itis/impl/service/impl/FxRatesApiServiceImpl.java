package ru.itis.impl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.dto.response.fxrates.FxRatesResponse;
import ru.itis.impl.properties.FxRatesApiProperties;
import ru.itis.impl.service.FxRatesApiService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FxRatesApiServiceImpl implements FxRatesApiService {
    private final FxRatesApiProperties config;
    private final RestTemplate restTemplate;

    public Map<String, Double> getLatestRates(String base) {
        String url = UriComponentsBuilder
                .fromHttpUrl(config.getBaseUrl())
                .queryParam("apikey", config.getApiKey())
                .queryParam("base", base)
                .build()
                .toUriString();

        FxRatesResponse response = restTemplate.getForObject(url, FxRatesResponse.class);
        return response != null ? response.rates() : null;
    }

    public Map<String, Double> getLatestRates(String base, String... symbols) {
        String url = UriComponentsBuilder
                .fromHttpUrl(config.getBaseUrl())
                .queryParam("apikey", config.getApiKey())
                .queryParam("base", base)
                .queryParam("symbols", String.join(",", symbols))
                .build()
                .toUriString();

        FxRatesResponse response = restTemplate.getForObject(url, FxRatesResponse.class);
        return response != null ? response.rates() : null;
    }
}