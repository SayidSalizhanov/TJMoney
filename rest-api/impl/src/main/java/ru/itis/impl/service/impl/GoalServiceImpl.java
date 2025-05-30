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
import ru.itis.impl.exception.AccessDeniedException;
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
import ru.itis.impl.service.GroupService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalMapper goalMapper;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    @Override
    @Transactional(readOnly = true)
    public List<GoalListResponse> getAll(Long userId, @MayBeNull Long groupId, Integer page, Integer amountPerPage) {
        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

        List<Goal> goals;

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("title"));

        if (groupId == null) goals = goalRepository.findAllByUserAndGroupIsNull(user, pageable).getContent();
        else {
            groupService.checkUserIsGroupMemberVoid(user, group);
            goals = goalRepository.findAllByGroup(group, pageable).getContent();
        }

        return goals.stream()
                .map(goalMapper::toGoalListResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GoalSettingsResponse getById(Long goalId, Long userId) {
        User user = requireUserById(userId);
        Goal goal = requireById(goalId);

        checkAccessToGoalGranted(goal, user);

        return goalMapper.toGoalSettingsResponse(goal);
    }

    @Override
    @Transactional
    public Long create(Long userId, @MayBeNull Long groupId, GoalCreateRequest request) {
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
    public void delete(Long id, Long userId) {
        User user = requireUserById(userId);
        Goal goal = requireById(id);

        checkAccessToGoalGranted(goal, user);

        goalRepository.delete(goal);
    }

    @Override
    @Transactional
    public void updateInfo(Long goalId, Long userId, GoalSettingsRequest request) {
        User user = requireUserById(userId);
        Goal goal = requireById(goalId);

        checkAccessToGoalGranted(goal, user);

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

    private void checkUserIsGoalOwner(Goal goal, User user) {
        if (!Objects.equals(goal.getUser().getId(), user.getId())) throw new AccessDeniedException("Доступ к личной цели имеет только ее владелец");
    }

    private void checkAccessToGoalGranted(Goal goal, User user) {
        Group group = goal.getGroup();
        if (group == null) checkUserIsGoalOwner(goal, user);
        else groupService.checkUserIsGroupMemberVoid(user, group);
    }
}
