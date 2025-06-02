package ru.itis.impl.service;

import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Map<String, Integer>> getGroupTransactionsGenerals(Long groupId, String period);
    TransactionSettingsResponse getById(Long id);
    void updateInfo(Long id, TransactionSettingsRequest request);
    void delete(Long id);
    List<TransactionListResponse> getAll(Long groupId, Integer page, Integer amountPerPage);
    Long create(Long groupId, TransactionCreateRequest request);
    List<Map<String, Integer>> getUserTransactionsGenerals(Long userId, String period);
}
