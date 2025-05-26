package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;

import java.util.List;

@RequestMapping("/reminders")
public interface ReminderApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ReminderSettingsResponse getReminder(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateReminderInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody ReminderSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReminder(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ReminderListResponse> getReminders(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "title") String sort
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createReminder(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestBody ReminderCreateRequest request
    );
}
