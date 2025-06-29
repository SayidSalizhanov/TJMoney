package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.reminder.ReminderCreateRequest;
import ru.itis.mainservice.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.mainservice.dto.response.reminder.ReminderListResponse;
import ru.itis.mainservice.dto.response.reminder.ReminderSettingsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;
    private static final String BASE_URL = "/api/reminders";

    public ReminderSettingsResponse getReminder(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ReminderSettingsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ReminderSettingsResponse.class
        );

        return response.getBody();
    }

    public void updateReminderInfo(Long id, ReminderSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReminderSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void deleteReminder(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public List<ReminderListResponse> getReminders(
            Long groupId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ReminderListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public Long createReminder(Long groupId, ReminderCreateRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/new");

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReminderCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                Long.class
        );

        return response.getBody();
    }
}