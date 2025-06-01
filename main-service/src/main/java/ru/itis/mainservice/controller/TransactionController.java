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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping("/{id}")
    public String getTransaction(
            @PathVariable Long id,
            @RequestParam Long userId,
            Model model
    ) {
        TransactionSettingsResponse transaction = transactionService.getTransaction(id, userId);
        model.addAttribute("transaction", transaction);
        model.addAttribute("id", id);
        model.addAttribute("userId", userId);
        return "transactions/transaction";
    }

    @PutMapping("/{id}")
    public String updateTransactionInfo(
            @PathVariable Long id,
            @RequestParam Long userId,
            TransactionSettingsRequest request
    ) {
        transactionService.updateTransactionInfo(id, userId, request);
        return "redirect:/transactions/" + id + "?userId=" + userId;
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        Long groupId = transactionService.getTransaction(id, userId).groupId();
        transactionService.deleteTransaction(id, userId);
        return "redirect:/transactions?userId=" + userId + (groupId != null ? "&groupId=" + groupId : "");
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
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "transactions/transactions";
    }

    @GetMapping("/new")
    public String getNewTransactionPage(
            @RequestParam Long userId,
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "transactions/new";
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
    public String getUploadTransactionsPage(
            @RequestParam Long userId,
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "transactions/upload";
    }

    @PostMapping("/new/upload")
    public String uploadCsvTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) Long groupId,
            @RequestParam MultipartFile file
    ) {
        transactionService.uploadCsvTransactions(userId, groupId, file);
        return "redirect:/transactions?userId=" + userId + (groupId != null ? "&groupId=" + groupId : "");
    }
} 