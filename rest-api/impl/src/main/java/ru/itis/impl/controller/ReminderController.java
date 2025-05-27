package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.ReminderApi;
import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;
import ru.itis.impl.service.ReminderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReminderController implements ReminderApi {

    private final ReminderService reminderService;

    @Override
    public ReminderSettingsResponse getReminder(Long id, Long userId) {
        return reminderService.getById(id, userId);
    }

    @Override
    public void updateReminderInfo(Long id, Long userId, ReminderSettingsRequest request) {
        reminderService.updateInfo(id, userId, request);
    }

    @Override
    public void deleteReminder(Long id, Long userId) {
        reminderService.delete(id, userId);
    }

    @Override
    public List<ReminderListResponse> getReminders(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort) {
        return reminderService.getAll(userId, groupId, page, amountPerPage, sort);
    }

    @Override
    public Long createReminder(Long userId, Long groupId, ReminderCreateRequest request) {
        return reminderService.create(userId, groupId, request);
    }
}
