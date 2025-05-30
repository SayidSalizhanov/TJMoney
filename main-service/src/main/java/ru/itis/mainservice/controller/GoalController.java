package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.goal.GoalCreateRequest;
import ru.itis.mainservice.dto.request.goal.GoalSettingsRequest;
import ru.itis.mainservice.dto.response.goal.GoalListResponse;
import ru.itis.mainservice.dto.response.goal.GoalSettingsResponse;
import ru.itis.mainservice.service.GoalService;

import java.util.List;

@Controller
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @GetMapping
    public String getGoalsPage(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<GoalListResponse> goals = goalService.getGoals(userId, groupId, page, size);
        model.addAttribute("goals", goals);
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "goals/list";
    }

    @GetMapping("/create")
    public String getCreateGoalPage(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("userId", userId);
        model.addAttribute("groupId", groupId);
        return "goals/create";
    }

    @PostMapping("/create")
    public String createGoal(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "groupId", required = false) Long groupId,
            GoalCreateRequest request) {

        goalService.createGoal(userId, groupId, request);
        return "redirect:/goals?userId=" + userId + (groupId != null ? "&groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getGoalPage(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            Model model) {

        GoalSettingsResponse goal = goalService.getGoal(id, userId);
        model.addAttribute("goal", goal);
        model.addAttribute("goalId", id);
        model.addAttribute("userId", userId);
        return "goals/details";
    }

    @PostMapping("/{id}/update")
    public String updateGoal(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            GoalSettingsRequest request) {

        goalService.updateGoalInfo(id, userId, request);
        return "redirect:/goals/" + id + "?userId=" + userId;
    }

    @PostMapping("/{id}/delete")
    public String deleteGoal(
            @PathVariable Long id,
            @RequestParam("userId") Long userId) {

        goalService.deleteGoal(id, userId);
        return "redirect:/goals?userId=" + userId;
    }
}