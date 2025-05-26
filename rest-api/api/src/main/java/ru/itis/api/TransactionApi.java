package ru.itis.api;

import jakarta.servlet.http.Part;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;

import java.util.List;

@RequestMapping("/transactions")
public interface TransactionApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TransactionSettingsResponse getTransaction(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateTransactionInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody TransactionSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTransaction(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TransactionListResponse> getTransactions(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "amount") String sort
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createTransaction(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestBody TransactionCreateRequest request
    );

    @PostMapping("/new/uploadTransactions")
    @ResponseStatus(HttpStatus.CREATED)
    List<Long> uploadXlsTransactions(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody Part xlsFile
    );
}
