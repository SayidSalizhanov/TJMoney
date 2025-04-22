package ru.itis.impl.controller;

import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.TransactionApi;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    @Override
    public TransactionSettingsResponse getTransaction(Long id) {
        return null;
    }

    @Override
    public void updateTransactionInfo(Long id, TransactionSettingsRequest request) {

    }

    @Override
    public void deleteTransaction(Long id) {

    }

    @Override
    public List<TransactionListResponse> getTransactions(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort) {
        return List.of();
    }

    @Override
    public Long createTransaction(Long userId, Long groupId, TransactionCreateRequest request) {
        return 0L;
    }

    @Override
    public List<Long> uploadXlsTransactions(Part xlsFile) {
        return List.of();
    }
}
