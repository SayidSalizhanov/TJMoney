package ru.itis.impl.service;

import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;

import java.util.List;

public interface ReminderService {
    ReminderSettingsResponse getById(Long id, Long userId);
    void updateInfo(Long id, Long userId, ReminderSettingsRequest request);
    void delete(Long id, Long userId);
    List<ReminderListResponse> getAll(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort);
    Long create(Long userId, Long groupId, ReminderCreateRequest request);
}
