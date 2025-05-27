package ru.itis.impl.service;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Map<String, Integer>> getGroupTransactionsGenerals(Long groupId, String period);
}
