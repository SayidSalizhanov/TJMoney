package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.transaction.TransactionCreateRequest;
import ru.itis.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.dto.response.transaction.TransactionListResponse;
import ru.itis.dto.response.transaction.TransactionSettingsResponse;

import java.util.List;

@RequestMapping("/api/transactions")
public interface TransactionApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    TransactionSettingsResponse getTransaction(
            @PathVariable("id") Long id
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateTransactionInfo(
            @PathVariable("id") Long id,
            @RequestBody TransactionSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTransaction(
            @PathVariable("id") Long id
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<TransactionListResponse> getTransactions(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createTransaction(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestBody TransactionCreateRequest request
    );

    @PostMapping("/new/uploadTransactions")
    @ResponseStatus(HttpStatus.CREATED)
    List<Long> uploadCsvTransactions(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestPart("file") MultipartFile csvFile
    );
}
