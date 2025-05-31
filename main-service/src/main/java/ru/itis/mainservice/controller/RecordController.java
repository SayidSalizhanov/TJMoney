package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<RecordListResponse> records = recordService.getRecords(userId, groupId, page, size);
        model.addAttribute("records", records);
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "records/records";
    }

    @GetMapping("/new")
    public String getCreateRecordPage(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "records/new";
    }

    @PostMapping("/new")
    public String createRecord(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            RecordCreateRequest request) {

        recordService.createRecord(userId, groupId, request);
        return "redirect:/records?userId=" + userId + (groupId != null ? "&groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getRecordPage(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            Model model) {

        RecordSettingsResponse record = recordService.getRecord(id, userId);
        model.addAttribute("record", record);
        model.addAttribute("recordId", id);
        model.addAttribute("userId", userId);
        return "records/record";
    }

    @PutMapping("/{id}")
    public String updateRecord(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            RecordSettingsRequest request) {

        recordService.updateRecordInfo(id, userId, request);
        return "redirect:/records/" + id + "?userId=" + userId;
    }

    @DeleteMapping("/{id}")
    public String deleteRecord(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {

        recordService.deleteRecord(id, userId);
        return "redirect:/records?userId=" + userId;
    }
}