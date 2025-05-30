package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mainservice.dto.request.user.UserPasswordChangeRequest;
import ru.itis.mainservice.dto.request.user.UserSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationToGroupResponse;
import ru.itis.mainservice.dto.response.user.UserGroupResponse;
import ru.itis.mainservice.dto.response.user.UserProfileResponse;
import ru.itis.mainservice.dto.response.user.UserSettingsResponse;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    public UserSettingsResponse getUserSettingsInfo(Long id, Long userId) {
        return restTemplate.getForObject(
            apiBaseUrl + "/api/user/{id}/settings?userId={userId}",
            UserSettingsResponse.class,
            id, userId
        );
    }

    public void updateUserSettingsInfo(Long id, Long userId, UserSettingsRequest request) {
        restTemplate.put(
            apiBaseUrl + "/api/user/{id}/settings?userId={userId}",
            request,
            id, userId
        );
    }

    public void deleteUser(Long id, Long userId) {
        restTemplate.delete(
            apiBaseUrl + "/api/user/{id}/settings?userId={userId}",
            id, userId
        );
    }

    public List<UserGroupResponse> getUserGroups(Long id, Long userId, Integer page, Integer amountPerPage) {
        UserGroupResponse[] groups = restTemplate.getForObject(
            apiBaseUrl + "/api/user/{id}/groups?userId={userId}&page={page}&amount_per_page={amountPerPage}",
            UserGroupResponse[].class,
            id, userId, page, amountPerPage
        );
        return groups != null ? Arrays.asList(groups) : List.of();
    }

    public List<ApplicationToGroupResponse> getUserApplicationsToGroup(Long id, Long userId, Integer page, Integer amountPerPage) {
        ApplicationToGroupResponse[] applications = restTemplate.getForObject(
            apiBaseUrl + "/api/user/{id}/applications?userId={userId}&page={page}&amount_per_page={amountPerPage",
            ApplicationToGroupResponse[].class,
            id, userId, page, amountPerPage
        );
        return applications != null ? Arrays.asList(applications) : List.of();
    }

    public void deleteUserApplicationToGroup(Long id, Long userId, Long applicationId) {
        restTemplate.delete(
            apiBaseUrl + "/api/user/{id}/applications?userId={userId}",
            applicationId,
            id, userId
        );
    }

    public void changeUserPassword(Long id, Long userId, UserPasswordChangeRequest request) {
        restTemplate.patchForObject(
            apiBaseUrl + "/api/user/{id}/changePassword?userId={userId}",
            request,
            Void.class,
            id, userId
        );
    }

    public String getUserAvatarUrl(Long id, Long userId) {
        return restTemplate.getForObject(
            apiBaseUrl + "/api/user/{id}/changeAvatar?userId={userId}",
            String.class,
            id, userId
        );
    }

    public void changeUserAvatarUrl(Long id, Long userId, MultipartFile avatarImage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatarImage", avatarImage.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.patchForObject(
            apiBaseUrl + "/api/user/{id}/changeAvatar?userId={userId}",
            requestEntity,
            Void.class,
            id, userId
        );
    }

    public void deleteUserAvatar(Long id, Long userId) {
        restTemplate.delete(
            apiBaseUrl + "/api/user/{id}/changeAvatar?userId={userId}",
            id, userId
        );
    }

    public UserProfileResponse getUserProfileInfo(Long id, Long userId, String period) {
        return restTemplate.getForObject(
            apiBaseUrl + "/api/user/{id}?userId={userId}&period={period}",
            UserProfileResponse.class,
            id, userId, period
        );
    }
} 