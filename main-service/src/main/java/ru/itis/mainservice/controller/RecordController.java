package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.record.RecordCreateRequest;
import ru.itis.mainservice.dto.request.record.RecordSettingsRequest;
import ru.itis.mainservice.dto.response.record.RecordListResponse;
import ru.itis.mainservice.dto.response.record.RecordSettingsResponse;
import ru.itis.mainservice.service.RecordService;

import java.util.List;

@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public String getRecordsPage(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") int size,
            Model model) {

        List<RecordListResponse> records = recordService.getRecords(groupId, page, size);
        model.addAttribute("records", records);
        model.addAttribute("groupId", groupId);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", size);
        return "records/records";
    }

    @GetMapping("/new")
    public String getCreateRecordPage(
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("groupId", groupId);
        return "records/new";
    }

    @PostMapping("/new")
    public String createRecord(
            @RequestParam(required = false) Long groupId,
            @Valid RecordCreateRequest request,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("groupId", groupId);
            model.addAttribute("error", errorMessage);
            return "records/new";
        }

        recordService.createRecord(groupId, request);
        return "redirect:/records" + (groupId != null ? "?groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getRecordPage(
            @PathVariable Long id,
            Model model) {

        RecordSettingsResponse record = recordService.getRecord(id);
        model.addAttribute("record", record);
        model.addAttribute("recordId", id);
        return "records/record";
    }

    @PutMapping("/{id}")
    public String updateRecord(@PathVariable Long id,
                             @Valid RecordSettingsRequest request,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");

            RecordSettingsResponse record = recordService.getRecord(id);
            model.addAttribute("record", record);
            model.addAttribute("recordId", id);
            model.addAttribute("error", errorMessage);
            return "records/record";
        }

        recordService.updateRecordInfo(id, request);
        return "redirect:/records/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id) {

        recordService.deleteRecord(id);
        return "redirect:/records";
    }
}