package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.api.TransactionApi;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;
import ru.itis.impl.service.CsvParsingService;
import ru.itis.impl.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;
    private final CsvParsingService csvParsingService;

    @Override
    public TransactionSettingsResponse getTransaction(Long id, Long userId) {
        return transactionService.getById(id, userId);
    }

    @Override
    public void updateTransactionInfo(Long id, Long userId, TransactionSettingsRequest request) {
        transactionService.updateInfo(id, userId, request);
    }

    @Override
    public void deleteTransaction(Long id, Long userId) {
        transactionService.delete(id, userId);
    }

    @Override
    public List<TransactionListResponse> getTransactions(Long userId, Long groupId, Integer page, Integer amountPerPage) {
        return transactionService.getAll(userId, groupId, page, amountPerPage);
    }

    @Override
    public Long createTransaction(Long userId, Long groupId, TransactionCreateRequest request) {
        return transactionService.create(userId, groupId, request);
    }

    @Override
    public List<Long> uploadCsvTransactions(Long userId, Long groupId, MultipartFile csvFile) {
        return csvParsingService.parseCsv(userId, groupId, csvFile);
    }
}
