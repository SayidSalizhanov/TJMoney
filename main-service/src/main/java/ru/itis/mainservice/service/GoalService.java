package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.goal.GoalCreateRequest;
import ru.itis.mainservice.dto.request.goal.GoalSettingsRequest;
import ru.itis.mainservice.dto.response.goal.GoalListResponse;
import ru.itis.mainservice.dto.response.goal.GoalSettingsResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/goals";

    public GoalSettingsResponse getGoal(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        return restTemplate.getForObject(url, GoalSettingsResponse.class);
    }

    public void updateGoalInfo(Long id, Long userId, GoalSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.put(url, request);
    }

    public void deleteGoal(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.delete(url);
    }

    public List<GoalListResponse> getGoals(
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

        ResponseEntity<List<GoalListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public Long createGoal(Long userId, Long groupId, GoalCreateRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
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