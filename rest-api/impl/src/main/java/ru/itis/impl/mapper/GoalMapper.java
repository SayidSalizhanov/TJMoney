package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;
import ru.itis.impl.model.Goal;

@Component
public class GoalMapper {

    public GoalSettingsResponse toGoalSettingsResponse(Goal goal) {
        return GoalSettingsResponse.builder()
                .title(goal.getTitle())
                .description(goal.getDescription())
                .progress(goal.getProgress())
                .build();
    }

    public GoalListResponse toGoalListResponse(Goal goal) {
        return GoalListResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .progress(goal.getProgress())
                .build();
    }
}
