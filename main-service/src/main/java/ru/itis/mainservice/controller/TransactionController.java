package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            Model model
    ) {
        TransactionSettingsResponse transaction = transactionService.getTransaction(id);
        model.addAttribute("transaction", transaction);
        model.addAttribute("id", id);
        return "transactions/transaction";
    }

    @PutMapping("/{id}")
    public String updateTransactionInfo(
            @PathVariable Long id,
            @Valid TransactionSettingsRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            TransactionSettingsResponse transaction = transactionService.getTransaction(id);
            model.addAttribute("transaction", transaction);
            model.addAttribute("id", id);
            return "transactions/transaction";
        }
        transactionService.updateTransactionInfo(id, request);
        return "redirect:/transactions/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        Long groupId = transactionService.getTransaction(id).groupId();
        transactionService.deleteTransaction(id);
        return "redirect:/transactions" + (groupId != null ? "?groupId=" + groupId : "");
    }

    @GetMapping
    public String getTransactions(
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<TransactionListResponse> transactions = transactionService.getTransactions(groupId, page, amountPerPage);
        model.addAttribute("transactions", transactions);
        model.addAttribute("groupId", groupId);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);
        return "transactions/transactions";
    }

    @GetMapping("/new")
    public String getNewTransactionPage(
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "transactions/new";
    }

    @PostMapping("/new")
    public String createTransaction(
            @RequestParam(required = false) Long groupId,
            @Valid TransactionCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            model.addAttribute("groupId", groupId);
            model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
            return "transactions/new";
        }

        Long transactionId = transactionService.createTransaction(groupId, request);
        return "redirect:/transactions/" + transactionId;
    }

    @GetMapping("/new/upload")
    public String getUploadTransactionsPage(
            @RequestParam(required = false) Long groupId,
            Model model
    ) {
        model.addAttribute("groupId", groupId);
        return "transactions/upload";
    }

    @PostMapping("/new/upload")
    public String uploadCsvTransactions(
            @RequestParam(required = false) Long groupId,
            @RequestParam MultipartFile file,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            model.addAttribute("groupId", groupId);
            return "transactions/upload";
        }
        transactionService.uploadCsvTransactions(groupId, file);
        return "redirect:/transactions" + (groupId != null ? "?groupId=" + groupId : "");
    }
} 