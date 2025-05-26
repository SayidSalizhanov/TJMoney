package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;

import java.util.List;

@RequestMapping("/goals")
public interface GoalApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GoalSettingsResponse getGoal(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateGoalInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody GoalSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGoal(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<GoalListResponse> getGoals(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "title") String sort
    );

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long createGoal(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestBody GoalCreateRequest request
    );
}
