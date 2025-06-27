package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.api.TransactionApi;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;
import ru.itis.dto.response.transaction.TransactionPredictResponse;
import ru.itis.impl.service.CsvParsingService;
import ru.itis.impl.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;
    private final CsvParsingService csvParsingService;

    @Override
    public TransactionSettingsResponse getTransaction(Long id) {
        return transactionService.getById(id);
    }

    @Override
    public void updateTransactionInfo(Long id, TransactionSettingsRequest request) {
        transactionService.updateInfo(id, request);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionService.delete(id);
    }

    @Override
    public List<TransactionListResponse> getTransactions(Long groupId, Integer page, Integer amountPerPage) {
        return transactionService.getAll(groupId, page, amountPerPage);
    }

    @Override
    public Long createTransaction(Long groupId, TransactionCreateRequest request) {
        return transactionService.create(groupId, request);
    }

    @Override
    public List<Long> uploadCsvTransactions(Long groupId, MultipartFile csvFile) {
        return csvParsingService.parseCsv(groupId, csvFile);
    }

    @Override
    public TransactionPredictResponse predictUserExpenses() {
        return transactionService.predictUserExpenses();
    }
}
