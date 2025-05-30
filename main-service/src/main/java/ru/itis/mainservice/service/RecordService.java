package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.record.RecordCreateRequest;
import ru.itis.mainservice.dto.request.record.RecordSettingsRequest;
import ru.itis.mainservice.dto.response.record.RecordListResponse;
import ru.itis.mainservice.dto.response.record.RecordSettingsResponse;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RecordService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/api/records";

    public RecordSettingsResponse getRecord(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        return restTemplate.getForObject(url, RecordSettingsResponse.class);
    }

    public void updateRecordInfo(Long id, Long userId, RecordSettingsRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.put(url, request);
    }

    public void deleteRecord(Long id, Long userId) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/{id}")
                .queryParam("userId", userId)
                .buildAndExpand(id)
                .toUriString();

        restTemplate.delete(url);
    }

    public List<RecordListResponse> getRecords(
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

        ResponseEntity<List<RecordListResponse>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody();
    }

    public Long createRecord(Long userId, Long groupId, RecordCreateRequest request) {
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