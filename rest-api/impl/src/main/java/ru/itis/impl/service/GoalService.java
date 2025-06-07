package ru.itis.impl.service;

import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;

import java.util.List;

public interface GoalService {
    List<GoalListResponse> getAll(Long groupId, Integer page, Integer amountPerPage);
    GoalSettingsResponse getById(Long goalId);
    Long create(Long groupId, GoalCreateRequest request);
    void delete(Long id);
    void updateInfo(Long goalId, GoalSettingsRequest request);
}
