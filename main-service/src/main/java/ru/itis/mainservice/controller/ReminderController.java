package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.reminder.ReminderCreateRequest;
import ru.itis.mainservice.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.mainservice.dto.response.reminder.ReminderListResponse;
import ru.itis.mainservice.dto.response.reminder.ReminderSettingsResponse;
import ru.itis.mainservice.service.ReminderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping
    public String getRemindersPage(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<ReminderListResponse> reminders = reminderService.getReminders(
                userId, groupId, page, size
        );

        model.addAttribute("reminders", reminders);
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "reminders/list";
    }

    @GetMapping("/create")
    public String getCreateReminderPage(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "reminders/create";
    }

    @PostMapping("/create")
    public String createReminder(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            ReminderCreateRequest request) {

        reminderService.createReminder(userId, groupId, request);
        return "redirect:/reminders?userId=" + userId + (groupId != null ? "&groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getReminderPage(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            Model model) {

        ReminderSettingsResponse reminder = reminderService.getReminder(id, userId);
        model.addAttribute("reminder", reminder);
        model.addAttribute("reminderId", id);
        model.addAttribute("userId", userId);
        model.addAttribute("formattedSendAt", reminder.sendAt().format(formatter));
        return "reminders/details";
    }

    @PostMapping("/{id}/update")
    public String updateReminder(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            ReminderSettingsRequest request) {

        reminderService.updateReminderInfo(id, userId, request);
        return "redirect:/reminders/" + id + "?userId=" + userId;
    }

    @PostMapping("/{id}/delete")
    public String deleteReminder(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {

        reminderService.deleteReminder(id, userId);
        return "redirect:/reminders?userId=" + userId;
    }
}