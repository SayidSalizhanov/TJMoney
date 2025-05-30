package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.reminder.ReminderCreateRequest;
import ru.itis.mainservice.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.mainservice.dto.response.reminder.ReminderListResponse;
import ru.itis.mainservice.dto.response.reminder.ReminderSettingsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/reminders";

    public ReminderSettingsResponse getReminder(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        return restTemplate.getForObject(url, ReminderSettingsResponse.class);
    }

    public void updateReminderInfo(Long id, Long userId, ReminderSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.put(url, request);
    }

    public void deleteReminder(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.delete(url);
    }

    public List<ReminderListResponse> getReminders(
            Long userId,
            Long groupId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("userId", userId)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        ResponseEntity<List<ReminderListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public Long createReminder(Long userId, Long groupId, ReminderCreateRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/new")
                .queryParam("userId", userId);

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        return restTemplate.postForObject(
                builder.toUriString(),
                request,
                Long.class
        );
    }
}