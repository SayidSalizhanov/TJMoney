package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import ru.itis.mainservice.dto.request.transaction.TransactionCreateRequest;
import ru.itis.mainservice.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.mainservice.dto.response.transaction.TransactionListResponse;
import ru.itis.mainservice.dto.response.transaction.TransactionSettingsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final RestTemplate restTemplate;
    private final AuthService authService;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private static final String BASE_URL = "/api/transactions";

    public TransactionSettingsResponse getTransaction(Long id) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<TransactionSettingsResponse> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}",
                HttpMethod.GET,
                entity,
                TransactionSettingsResponse.class,
                id
        );

        return response.getBody();
    }

    public void updateTransactionInfo(Long id, TransactionSettingsRequest request) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransactionSettingsRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/{id}",
                HttpMethod.PUT,
                entity,
                Void.class,
                id
        );
    }

    public void deleteTransaction(Long id) {
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

    public List<TransactionListResponse> getTransactions(
            Long groupId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL)
                .queryParam("groupId", groupId)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<TransactionListResponse>> response = restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return Optional.ofNullable(response.getBody())
                .orElse(Collections.emptyList());
    }

    public Long createTransaction(Long groupId, TransactionCreateRequest request) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransactionCreateRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Long> response = restTemplate.exchange(
                apiBaseUrl + BASE_URL + "/new?groupId={groupId}",
                HttpMethod.POST,
                entity,
                Long.class,
                groupId
        );

        return response.getBody();
    }

    public List<Long> uploadCsvTransactions(Long groupId, MultipartFile csvFile) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", csvFile.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
            apiBaseUrl + BASE_URL + "/new/uploadTransactions&groupId={groupId}",
            HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<List<Long>>() {},
            groupId
        ).getBody();
    }
} 