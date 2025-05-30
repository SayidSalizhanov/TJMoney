package ru.itis.mainservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final RestTemplate restTemplate;

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private static final String BASE_URL = "/api/transactions";

    public TransactionSettingsResponse getTransaction(Long id, Long userId) {
        return restTemplate.getForObject(
            apiBaseUrl + BASE_URL + "/{id}?userId={userId}",
            TransactionSettingsResponse.class,
            id, userId
        );
    }

    public void updateTransactionInfo(Long id, Long userId, TransactionSettingsRequest request) {
        restTemplate.put(
            apiBaseUrl + BASE_URL + "/{id}?userId={userId}",
            request,
            id, userId
        );
    }

    public void deleteTransaction(Long id, Long userId) {
        restTemplate.delete(
            apiBaseUrl + BASE_URL + "/{id}?userId={userId}",
            id, userId
        );
    }

    public List<TransactionListResponse> getTransactions(
            Long userId,
            Long groupId,
            Integer page,
            Integer amountPerPage
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiBaseUrl + BASE_URL)
                .queryParam("userId", userId)
                .queryParam("groupId", groupId)
                .queryParam("page", page)
                .queryParam("amount_per_page", amountPerPage);

        return restTemplate.exchange(
                builder.build().toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionListResponse>>() {}
        ).getBody();
    }

    public Long createTransaction(Long userId, Long groupId, TransactionCreateRequest request) {
        return restTemplate.postForObject(
            apiBaseUrl + BASE_URL + "/new?userId={userId}&groupId={groupId}",
            request,
            Long.class,
            userId, groupId
        );
    }

    public List<Long> uploadCsvTransactions(Long userId, Long groupId, MultipartFile csvFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", csvFile.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
            apiBaseUrl + BASE_URL + "/new/uploadTransactions?userId={userId}&groupId={groupId}",
            HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<List<Long>>() {},
            userId, groupId
        ).getBody();
    }
} 