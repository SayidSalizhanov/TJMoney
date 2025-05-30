package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.application.ApplicationAnswerRequest;
import ru.itis.mainservice.dto.request.group.GroupCreateRequest;
import ru.itis.mainservice.dto.request.group.GroupSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.mainservice.dto.response.group.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private static final String BASE_URL = "/api/groups";

    public List<GroupListResponse> getGroupsWhereUserNotJoined(Long userId) {
        return restTemplate.exchange(
            apiBaseUrl + BASE_URL + "?userId={userId}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<GroupListResponse>>() {},
            userId
        ).getBody();
    }

    public Long createApplicationToGroup(Long userId, Long groupId) {
        return restTemplate.postForObject(
            apiBaseUrl + BASE_URL + "?userId={userId}&groupId={groupId}",
            null,
            Long.class,
            userId, groupId
        );
    }

    public GroupProfileResponse getGroup(Long id, Long userId, String period) {
        return restTemplate.getForObject(
            apiBaseUrl + BASE_URL + "/{id}?userId={userId}&period={period}",
            GroupProfileResponse.class,
            id, userId, period
        );
    }

    public void leaveGroup(Long id, Long userId) {
        restTemplate.delete(
            apiBaseUrl + BASE_URL + "/{id}?userId={userId}",
            id, userId
        );
    }

    public GroupSettingsResponse getGroupSettings(Long id, Long userId) {
        return restTemplate.getForObject(
            apiBaseUrl + BASE_URL + "/{id}/settings?userId={userId}",
            GroupSettingsResponse.class,
            id, userId
        );
    }

    public void updateGroupInfo(Long id, Long userId, GroupSettingsRequest request) {
        restTemplate.put(
            apiBaseUrl + BASE_URL + "/{id}/settings?userId={userId}",
            request,
            id, userId
        );
    }

    public void deleteGroup(Long id, Long userId) {
        restTemplate.delete(
            apiBaseUrl + BASE_URL + "/{id}/settings?userId={userId}",
            id, userId
        );
    }

    public GroupViewingResponse getGroupView(Long id, Long userId) {
        return restTemplate.getForObject(
            apiBaseUrl + BASE_URL + "/{id}/viewing?userId={userId}",
            GroupViewingResponse.class,
            id, userId
        );
    }

    public Long createGroup(Long userId, GroupCreateRequest request) {
        return restTemplate.postForObject(
            apiBaseUrl + BASE_URL + "/new?userId={userId}",
            request,
            Long.class,
            userId
        );
    }

    public List<GroupMemberResponse> getGroupMembers(
            Long id,
            Long userId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}/members")
                .queryParam("userId", userId)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        return restTemplate.exchange(
                builder.buildAndExpand(id).toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GroupMemberResponse>>() {}
        ).getBody();
    }

    public void deleteMemberFromAdminSide(Long id, Long userId, Long userIdForDelete) {
        restTemplate.delete(
            apiBaseUrl + BASE_URL + "/{id}/members?userId={userId}&userIdForDelete={userIdForDelete}",
            id, userId, userIdForDelete
        );
    }

    public List<ApplicationWithUserInfoResponse> getApplications(
            Long id,
            Long userId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}/applications")
                .queryParam("userId", userId)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        return restTemplate.exchange(
                builder.buildAndExpand(id).toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ApplicationWithUserInfoResponse>>() {}
        ).getBody();
    }

    public Long answerApplication(Long id, Long userId, ApplicationAnswerRequest request) {
        return restTemplate.postForObject(
            apiBaseUrl + BASE_URL + "/{id}/applications?userId={userId}",
            request,
            Long.class,
            id, userId
        );
    }
} 