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
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        List<GoalListResponse> goals = goalService.getGoals(groupId, page, size);
        model.addAttribute("goals", goals);
        model.addAttribute("groupId", groupId);
        return "goals/goals";
    }

    @GetMapping("/new")
    public String getCreateGoalPage(
            @RequestParam(value = "groupId", required = false) Long groupId,
            Model model) {

        model.addAttribute("groupId", groupId);
        return "goals/new";
    }

    @PostMapping("/new")
    public String createGoal(
            @RequestParam(value = "groupId", required = false) Long groupId,
            GoalCreateRequest request) {

        goalService.createGoal(groupId, request);
        return "redirect:/goals" + (groupId != null ? "?groupId=" + groupId : "");
    }

    @GetMapping("/{id}")
    public String getGoalPage(
            @PathVariable Long id,
            Model model) {

        GoalSettingsResponse goal = goalService.getGoal(id);
        model.addAttribute("goal", goal);
        model.addAttribute("goalId", id);
        return "goals/goal";
    }

    @PutMapping("/{id}")
    public String updateGoal(
            @PathVariable Long id,
            GoalSettingsRequest request) {

        goalService.updateGoalInfo(id, request);
        return "redirect:/goals/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteGoal(
            @PathVariable Long id) {

        goalService.deleteGoal(id);
        return "redirect:/goals";
    }
}