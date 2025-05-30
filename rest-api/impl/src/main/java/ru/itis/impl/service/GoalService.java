package ru.itis.impl.service;

import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;

import java.util.List;

public interface GoalService {
    List<GoalListResponse> getAll(Long userId, Long groupId, Integer page, Integer amountPerPage);
    GoalSettingsResponse getById(Long goalId, Long userId);
    Long create(Long userId, Long groupId, GoalCreateRequest request);
    void delete(Long id, Long userId);
    void updateInfo(Long goalId, Long userId, GoalSettingsRequest request);
}
