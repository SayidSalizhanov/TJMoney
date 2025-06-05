package ru.itis.impl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.dto.response.fxrates.FxRatesResponse;
import ru.itis.impl.properties.FxRatesApiProperties;
import ru.itis.impl.service.FxRatesApiService;

import java.io.IOException;
import java.util.Map;

@Service
public class FxRatesApiServiceImpl implements FxRatesApiService {
    private final FxRatesApiProperties config;
    private final RestTemplate restTemplate;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public FxRatesApiServiceImpl(
            FxRatesApiProperties config,
            RestTemplate restTemplate,
            OkHttpClient httpClient,
            @Qualifier("currencyObjectMapper") ObjectMapper objectMapper
    ) {
        this.config = config;
        this.restTemplate = restTemplate;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public Map<String, Double> getLatestRates(String base) {
        String url = UriComponentsBuilder
                .fromHttpUrl(config.getBaseUrl())
                .queryParam("apikey", config.getApiKey())
                .queryParam("base", base)
                .build()
                .toUriString();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }
            FxRatesResponse fxRatesResponse = objectMapper.readValue(response.body().string(), FxRatesResponse.class);
            return fxRatesResponse != null ? fxRatesResponse.rates() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
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