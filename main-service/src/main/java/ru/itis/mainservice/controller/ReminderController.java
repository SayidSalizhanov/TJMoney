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
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<ReminderListResponse> reminders = reminderService.getReminders(groupId, page, size);

        model.addAttribute("reminders", reminders);
        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "reminders/reminders";
    }

    @GetMapping("/new")
    public String getCreateReminderPage(
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("groupId", groupId);
        model.addAttribute("currentDateTime", LocalDateTime.now().format(formatter));
        return "reminders/new";
    }

    @PostMapping("/new")
    public String createReminder(
            @RequestParam(value = "groupId", required = false) Long groupId,
            ReminderCreateRequest request) {

        reminderService.createReminder(groupId, request);
        return "redirect:/reminders" + (groupId != null ? "?groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getReminderPage(
            @PathVariable Long id,
            Model model) {

        ReminderSettingsResponse reminder = reminderService.getReminder(id);
        model.addAttribute("reminder", reminder);
        model.addAttribute("reminderId", id);
        model.addAttribute("formattedSendAt", reminder.sendAt());
        return "reminders/reminder";
    }

    @PutMapping("/{id}")
    public String updateReminder(
            @PathVariable Long id,
            ReminderSettingsRequest request) {

        reminderService.updateReminderInfo(id, request);
        return "redirect:/reminders/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteReminder(@PathVariable Long id) {

        reminderService.deleteReminder(id);
        return "redirect:/reminders";
    }
}