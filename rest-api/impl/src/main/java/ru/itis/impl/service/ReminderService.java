package ru.itis.impl.service;

import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;

import java.util.List;

public interface ReminderService {
    ReminderSettingsResponse getById(Long id);
    void updateInfo(Long id, ReminderSettingsRequest request);
    void delete(Long id);
    List<ReminderListResponse> getAll(Long groupId, Integer page, Integer amountPerPage);
    Long create(Long groupId, ReminderCreateRequest request);
}
