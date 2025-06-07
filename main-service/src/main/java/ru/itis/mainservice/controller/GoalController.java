package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "amount_per_page", defaultValue = "10", required = false) int size,
            Model model) {

        List<GoalListResponse> goals = goalService.getGoals(groupId, page, size);
        model.addAttribute("goals", goals);
        model.addAttribute("groupId", groupId);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", size);
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
            @RequestParam(required = false) Long groupId,
            @Valid GoalCreateRequest request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("groupId", groupId);
            model.addAttribute("error", errorMessage);
            return "goals/new";
        }

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
    public String updateGoal(@PathVariable Long id,
                           @Valid GoalSettingsRequest request,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");

            GoalSettingsResponse goal = goalService.getGoal(id);
            model.addAttribute("goal", goal);
            model.addAttribute("goalId", id);
            model.addAttribute("error", errorMessage);
            return "goals/goal";
        }

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