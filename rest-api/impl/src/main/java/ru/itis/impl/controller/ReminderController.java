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
    public ReminderSettingsResponse getReminder(Long id) {
        return reminderService.getById(id);
    }

    @Override
    public void updateReminderInfo(Long id, ReminderSettingsRequest request) {
        reminderService.updateInfo(id, request);
    }

    @Override
    public void deleteReminder(Long id) {
        reminderService.delete(id);
    }

    @Override
    public List<ReminderListResponse> getReminders(Long groupId, Integer page, Integer amountPerPage) {
        return reminderService.getAll(groupId, page, amountPerPage);
    }

    @Override
    public Long createReminder(Long groupId, ReminderCreateRequest request) {
        return reminderService.create(groupId, request);
    }
}
