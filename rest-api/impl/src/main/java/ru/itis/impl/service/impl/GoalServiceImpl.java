package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.not_found.GoalNotFoundException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.GoalMapper;
import ru.itis.impl.model.Goal;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GoalRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.GoalService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GoalMapper goalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GoalListResponse> getByUserOrGroup(Long userId, @MayBeNull Long groupId, Integer page, Integer amountPerPage, String sort) {
        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by(sort));

        if (groupId == null) {
            User user = requireUserById(userId);

            return goalRepository.findByUser(user, pageable).stream()
                    .map(goalMapper::toGoalListResponse)
                    .toList();
        }

        Group group = requireGroupById(groupId);

        return goalRepository.findByGroup(group, pageable).stream()
                .map(goalMapper::toGoalListResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GoalSettingsResponse getGoal(Long goalId) {
        Goal goal = requireById(goalId);
        return goalMapper.toGoalSettingsResponse(goal);
    }

    @Override
    @Transactional
    public Long save(Long userId, @MayBeNull Long groupId, GoalCreateRequest request) {
        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

        Goal goal = Goal.builder()
                .title(request.title())
                .description(request.description())
                .progress(request.progress())
                .user(user)
                .group(group)
                .build();

        return goalRepository.save(goal).getId();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(GoalSettingsRequest request, Long goalId) {
        Goal goal = requireById(goalId);
        goal.setTitle(request.title());
        goal.setDescription(request.description());
        goal.setProgress(request.progress());
        goalRepository.save(goal);
    }

    private Goal requireById(Long goalId) {
        Optional<Goal> optionalGoal = goalRepository.findById(goalId);
        if (optionalGoal.isEmpty()) throw new GoalNotFoundException("Цель с таким id не найдена");
        return optionalGoal.get();
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }
}
