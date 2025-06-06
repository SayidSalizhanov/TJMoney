package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.itis.mainservice.dto.request.user.UserLoginRequest;
import ru.itis.mainservice.dto.response.security.JwtResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;
import ru.itis.mainservice.dto.response.user.CheckAdminStatusResponse;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private final RestTemplate restTemplate;
    private static final String TOKEN_KEY = "auth_token";

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
                HttpSession session = getSession();
                session.setAttribute(TOKEN_KEY, response.token());
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
            headers.setBearerAuth(getToken());
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);
            
            restTemplate.postForObject(
                apiBaseUrl + "/api/auth/logout",
                requestEntity,
                Void.class
            );
        } finally {
            HttpSession session = getSession();
            session.removeAttribute(TOKEN_KEY);
        }
    }

    public HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
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

    private String getToken() {
        HttpSession session = getSession();
        return (String) session.getAttribute(TOKEN_KEY);
    }

    private HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("No request context found");
        }
        return attributes.getRequest().getSession(true);
    }

    public boolean checkAdminRole() {
        String url = apiBaseUrl + "/api/user/role/admin";

        HttpHeaders headers = getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CheckAdminStatusResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CheckAdminStatusResponse.class
        );

        return Boolean.TRUE.equals(response.getBody().status());
    }
}
