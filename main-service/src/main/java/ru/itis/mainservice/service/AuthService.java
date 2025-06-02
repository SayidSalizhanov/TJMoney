package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.mainservice.dto.request.user.UserLoginRequest;
import ru.itis.mainservice.dto.response.security.JwtResponse;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private final RestTemplate restTemplate;
    private String token;

    public boolean login(UserLoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UserLoginRequest> requestEntity = new HttpEntity<>(request, headers);

            JwtResponse response = restTemplate.postForObject(
                apiBaseUrl + "/api/auth/login",
                requestEntity,
                JwtResponse.class
            );

            if (response != null && response.token() != null) {
                this.token = response.token();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            
            restTemplate.postForObject(
                apiBaseUrl + "/api/auth/logout",
                requestEntity,
                Void.class
            );
        } finally {
            token = null;
        }
    }

    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return headers;
    }

    public Long getAuthenticatedUserId() {
        HttpEntity<?> entity = new HttpEntity<>(getAuthHeaders());
        return restTemplate.exchange(
                apiBaseUrl + "/api/auth/userId",
                org.springframework.http.HttpMethod.GET,
                entity,
                Long.class
        ).getBody();
    }
}
