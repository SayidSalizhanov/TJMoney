package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.application.ApplicationAnswerRequest;
import ru.itis.mainservice.dto.request.group.GroupCreateRequest;
import ru.itis.mainservice.dto.request.group.GroupSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.mainservice.dto.response.group.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;
    private static final String BASE_URL = "/api/groups";

    public List<GroupListResponse> getGroupsWhereUserNotJoined(Integer page, Integer amountPerPage) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GroupListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<GroupListResponse>>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public Long createApplicationToGroup(Long groupId) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "?groupId={groupId}",
                HttpMethod.POST,
                entity,
                Long.class,
                groupId
        );

        return response.getBody();
    }

    public GroupProfileResponse getGroup(Long id, String period) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GroupProfileResponse> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}?period={period}",
                HttpMethod.GET,
                entity,
                GroupProfileResponse.class,
                id, period
        );

        return response.getBody();
    }

    public void leaveGroup(Long id) {
        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}",
                HttpMethod.DELETE,
                entity,
                Void.class,
                id
        );
    }

    public GroupSettingsResponse getGroupSettings(Long id) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GroupSettingsResponse> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/settings",
                HttpMethod.GET,
                entity,
                GroupSettingsResponse.class,
                id
        );

        return response.getBody();
    }

    public void updateGroupInfo(Long id, GroupSettingsRequest request) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GroupSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/settings",
                HttpMethod.PUT,
                entity,
                Void.class,
                id
        );
    }

    public void deleteGroup(Long id) {
        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/settings",
                HttpMethod.DELETE,
                entity,
                Void.class,
                id
        );
    }

    public GroupViewingResponse getGroupView(Long id) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GroupViewingResponse> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/viewing",
                HttpMethod.GET,
                entity,
                GroupViewingResponse.class,
                id
        );

        return response.getBody();
    }

    public Long createGroup(GroupCreateRequest request) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GroupCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/new",
                HttpMethod.POST,
                entity,
                Long.class
        );

        return response.getBody();
    }

    public List<GroupMemberResponse> getGroupMembers(
            Long id,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}/members")
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<GroupMemberResponse>> response = restTemplate.exchange(
                builder.buildAndExpand(id).toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public void deleteMemberFromAdminSide(Long id, Long userIdForDelete) {
        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/members?userIdForDelete={userIdForDelete}",
                HttpMethod.DELETE,
                entity,
                Void.class,
                id, userIdForDelete
        );
    }

    public List<ApplicationWithUserInfoResponse> getApplications(
            Long id,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}/applications")
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ApplicationWithUserInfoResponse>> response = restTemplate.exchange(
                builder.buildAndExpand(id).toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public Long answerApplication(Long id, ApplicationAnswerRequest request) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ApplicationAnswerRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}/applications",
                HttpMethod.POST,
                entity,
                Long.class,
                id
        );

        return response.getBody();
    }
} 