package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mainservice.dto.request.user.UserPasswordChangeRequest;
import ru.itis.mainservice.dto.request.user.UserSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationToGroupResponse;
import ru.itis.mainservice.dto.response.avatar.AvatarResponse;
import ru.itis.mainservice.dto.response.user.UserGroupResponse;
import ru.itis.mainservice.dto.response.user.UserProfileResponse;
import ru.itis.mainservice.dto.response.user.UserSettingsResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    public UserSettingsResponse getUserSettingsInfo(Long id) {
        String url = apiBaseUrl + "/api/user/{id}/settings";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserSettingsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserSettingsResponse.class,
                id
        );

        return response.getBody();
    }

    public void updateUserSettingsInfo(Long id, UserSettingsRequest request) {
        String url = apiBaseUrl + "/api/user/{id}/settings";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<UserSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class,
                id
        );
    }

    public void deleteUser(Long id) {
        String url = apiBaseUrl + "/api/user/{id}/settings";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class,
                id
        );
    }

    public List<UserGroupResponse> getUserGroups(Long id, Integer page, Integer amountPerPage) {
        String url = apiBaseUrl + "/api/user/{id}/groups?page={page}&amount_per_page={amountPerPage}";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserGroupResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserGroupResponse[].class,
                id, page, amountPerPage
        );

        return Optional.ofNullable(response.getBody())
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public List<ApplicationToGroupResponse> getUserApplicationsToGroup(Long id, Integer page, Integer amountPerPage) {
        String url = apiBaseUrl + "/api/user/{id}/applications?page={page}&amount_per_page={amountPerPage}";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ApplicationToGroupResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ApplicationToGroupResponse[].class,
                id, page, amountPerPage
        );

        return Optional.ofNullable(response.getBody())
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    public void deleteUserApplicationToGroup(Long id, Long applicationId) {
        String url = apiBaseUrl + "/api/user/{id}/applications?applicationId={applicationId}";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class,
                id, applicationId
        );
    }

    public void changeUserPassword(Long id, UserPasswordChangeRequest request) {
        String url = apiBaseUrl + "/api/user/{id}/changePassword";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<UserPasswordChangeRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class,
                id
        );
    }

    public String getUserAvatarUrl(Long id) {
        String url = apiBaseUrl + "/api/user/{id}/changeAvatar";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<AvatarResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                AvatarResponse.class,
                id
        );

        return Optional.ofNullable(response.getBody())
                .map(AvatarResponse::url)
                .orElse(null);
    }

    public void changeUserAvatarUrl(Long id, MultipartFile avatarImage) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatarImage", avatarImage.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.put(
            apiBaseUrl + "/api/user/{id}/changeAvatar",
            requestEntity,
            id
        );
    }

    public void deleteUserAvatar(Long id) {
        String url = apiBaseUrl + "/api/user/{id}/changeAvatar";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class,
                id
        );
    }

    public UserProfileResponse getUserProfileInfo(Long id, String period) {
        String url = apiBaseUrl + "/api/user/{id}?period={period}";

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<UserProfileResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserProfileResponse.class,
                id, period
        );

        return response.getBody();
    }
} 