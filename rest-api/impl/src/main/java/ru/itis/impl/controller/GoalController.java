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
    public GoalSettingsResponse getGoal(Long id) {
        return goalService.getById(id);
    }

    @Override
    public void updateGoalInfo(Long id, GoalSettingsRequest request) {
        goalService.updateInfo(id, request);
    }

    @Override
    public void deleteGoal(Long id) {
        goalService.delete(id);
    }

    @Override
    public List<GoalListResponse> getGoals(Long groupId, Integer page, Integer amountPerPage) {
        return goalService.getAll(groupId, page, amountPerPage);
    }

    @Override
    public Long createGoal(Long groupId, GoalCreateRequest request) {
        return goalService.create(groupId, request);
    }
}
