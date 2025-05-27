package ru.itis.impl.service;

import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Map<String, Integer>> getGroupTransactionsGenerals(Long groupId, String period);
    TransactionSettingsResponse getById(Long id, Long userId);
    void updateInfo(Long id, Long userId, TransactionSettingsRequest request);
    void delete(Long id, Long userId);
    List<TransactionListResponse> getAll(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort);
    Long create(Long userId, Long groupId, TransactionCreateRequest request);
    List<Map<String, Integer>> getUserTransactionsGenerals(Long userId, String period);
}
