package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.goal.GoalCreateRequest;
import ru.itis.mainservice.dto.request.goal.GoalSettingsRequest;
import ru.itis.mainservice.dto.response.goal.GoalListResponse;
import ru.itis.mainservice.dto.response.goal.GoalSettingsResponse;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;
    private static final String BASE_URL = "/api/goals";

    public GoalSettingsResponse getGoal(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GoalSettingsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GoalSettingsResponse.class
        );

        return response.getBody();
    }

    public void updateGoalInfo(Long id, GoalSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GoalSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }

    public void deleteGoal(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public List<GoalListResponse> getGoals(
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

        ResponseEntity<List<GoalListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public Long createGoal(Long groupId, GoalCreateRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/new");

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GoalCreateRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                Long.class
        ).getBody();
    }
}