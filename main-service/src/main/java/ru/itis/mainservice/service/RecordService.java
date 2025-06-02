package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.record.RecordCreateRequest;
import ru.itis.mainservice.dto.request.record.RecordSettingsRequest;
import ru.itis.mainservice.dto.response.record.RecordListResponse;
import ru.itis.mainservice.dto.response.record.RecordSettingsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RecordService {

    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;
    private static final String BASE_URL = "/api/records";

    public RecordSettingsResponse getRecord(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<RecordSettingsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                RecordSettingsResponse.class
        );

        return response.getBody();
    }

    public void updateRecordInfo(Long id, RecordSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RecordSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    public void deleteRecord(Long id) {
        String url = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/{id}")
                .buildAndExpand(id)
                .toUriString();

        HttpHeaders headers = authService.getAuthHeaders();

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public List<RecordListResponse> getRecords(
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

        ResponseEntity<List<RecordListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public Long createRecord(Long groupId, RecordCreateRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL + "/new");

        if (groupId != null) {
            builder.queryParam("groupId", groupId);
        }

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<RecordCreateRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                Long.class
        ).getBody();
    }
}