package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;

import java.util.List;

@RequestMapping("/api/reminders")
public interface ReminderApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ReminderSettingsResponse getReminder(
            @PathVariable("id") Long id
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateReminderInfo(
            @PathVariable("id") Long id,
            @RequestBody ReminderSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReminder(
            @PathVariable("id") Long id
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ReminderListResponse> getReminders(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createReminder(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestBody ReminderCreateRequest request
    );
}
