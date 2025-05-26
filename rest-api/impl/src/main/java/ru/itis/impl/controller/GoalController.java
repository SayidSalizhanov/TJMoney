package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.GoalApi;
import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;
import ru.itis.impl.service.GoalService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GoalController implements GoalApi {

    private final GoalService goalService;

    @Override
    public GoalSettingsResponse getGoal(Long id, Long userId) {
        return goalService.getGoal(id);
    }

    @Override
    public void updateGoalInfo(Long id, Long userId, GoalSettingsRequest request) {
        goalService.update(request, id);
    }

    @Override
    public void deleteGoal(Long id, Long userId) {
        goalService.delete(id);
    }

    @Override
    public List<GoalListResponse> getGoals(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort) {
        return goalService.getByUserOrGroup(userId, groupId, page, amountPerPage, sort);
    }

    @Override
    public Long createGoal(Long userId, Long groupId, GoalCreateRequest request) {
        return goalService.save(userId, groupId, request);
    }
}
