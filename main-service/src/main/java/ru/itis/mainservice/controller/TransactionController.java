package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.mainservice.dto.request.transaction.TransactionCreateRequest;
import ru.itis.mainservice.dto.request.transaction.TransactionSettingsRequest;
import ru.itis.mainservice.dto.response.transaction.TransactionListResponse;
import ru.itis.mainservice.dto.response.transaction.TransactionSettingsResponse;
import ru.itis.mainservice.service.TransactionService;

import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public String getTransaction(
            @PathVariable Long id,
            @RequestParam Long userId,
            Model model
    ) {
        TransactionSettingsResponse transaction = transactionService.getTransaction(id, userId);
        model.addAttribute("transaction", transaction);
        return "transaction/transaction";
    }

    @PutMapping("/{id}")
    public String updateTransactionInfo(
            @PathVariable Long id,
            @RequestParam Long userId,
            @ModelAttribute TransactionSettingsRequest request
    ) {
        transactionService.updateTransactionInfo(id, userId, request);
        return "redirect:/transactions/" + id + "?userId=" + userId;
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        transactionService.deleteTransaction(id, userId);
        return "redirect:/transactions";
    }

    @GetMapping
    public String getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<TransactionListResponse> transactions = transactionService.getTransactions(
                userId, groupId, page, amountPerPage
        );
        model.addAttribute("transactions", transactions);
        return "transaction/transactions";
    }

    @GetMapping("/new")
    public String getNewTransactionPage() {
        return "transaction/new";
    }

    @PostMapping("/new")
    public String createTransaction(
            @RequestParam Long userId,
            @RequestParam(required = false) Long groupId,
            @ModelAttribute TransactionCreateRequest request
    ) {
        Long transactionId = transactionService.createTransaction(userId, groupId, request);
        return "redirect:/transactions/" + transactionId + "?userId=" + userId;
    }

    @GetMapping("/new/upload")
    public String getUploadTransactionsPage() {
        return "transaction/upload";
    }

    @PostMapping("/new/upload")
    public String uploadCsvTransactions(
            @RequestParam Long userId,
            @RequestParam Long groupId,
            @RequestParam MultipartFile file
    ) {
        transactionService.uploadCsvTransactions(userId, groupId, file);
        return "redirect:/transactions?userId=" + userId + "&groupId=" + groupId;
    }
} 