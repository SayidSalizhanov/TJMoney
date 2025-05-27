package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;
import ru.itis.impl.model.Transaction;

@Component
public class TransactionMapper {

    public TransactionSettingsResponse toTransactionSettingsResponse(Transaction transaction) {
        return TransactionSettingsResponse.builder()
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .category(transaction.getCategory())
                .dateTime(transaction.getDateTime())
                .ownerName(transaction.getUser().getUsername())
                .description(transaction.getDescription())
                .build();
    }

    public TransactionListResponse toTransactionListResponse(Transaction transaction) {
        return TransactionListResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .category(transaction.getCategory())
                .dateTime(transaction.getDateTime())
                .username(transaction.getUser().getUsername())
                .type(transaction.getType())
                .build();
    }
}
