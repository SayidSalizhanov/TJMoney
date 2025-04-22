package ru.itis.impl.service;

import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;

import java.util.List;

public interface GoalService {
    List<GoalListResponse> getByUserOrGroup(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort);
    GoalSettingsResponse getGoal(Long goalId);
    Long save(Long userId, Long groupId, GoalCreateRequest request);
    void delete(Long id);
    void update(GoalSettingsRequest request, Long goalId);
}
