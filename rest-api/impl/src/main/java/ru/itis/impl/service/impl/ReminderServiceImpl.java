package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.reminder.ReminderCreateRequest;
import ru.itis.dto.request.reminder.ReminderSettingsRequest;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.AccessDeniedException;
import ru.itis.impl.exception.DateTimeOperationException;
import ru.itis.impl.exception.not_found.ReminderNotFoundException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.ReminderMapper;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Reminder;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.ReminderRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.GroupService;
import ru.itis.impl.service.ReminderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderMapper reminderMapper;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public ReminderSettingsResponse getById(Long id) {
        User user = requireUserById(authService.getAuthenticatedUserId());
        Reminder reminder = requireById(id);

        checkAccessToReminderGranted(reminder, user);

        return reminderMapper.toReminderSettingsResponse(reminder);
    }

    @Override
    @Transactional
    public void updateInfo(Long id, ReminderSettingsRequest request) {
        User user = requireUserById(authService.getAuthenticatedUserId());
        Reminder reminder = requireById(id);

        checkAccessToReminderGranted(reminder, user);

        reminder.setTitle(request.title());
        reminder.setMessage(request.message());
        reminder.setSendAt(request.sendAt());
        reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = requireUserById(authService.getAuthenticatedUserId());
        Reminder reminder = requireById(id);

        checkAccessToReminderGranted(reminder, user);

        reminderRepository.delete(reminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReminderListResponse> getAll(@MayBeNull Long groupId, Integer page, Integer amountPerPage) {
        User user = requireUserById(authService.getAuthenticatedUserId());
        Group group = groupId == null ? null : requireGroupById(groupId);

        List<Reminder> reminders;

        Pageable pageable = PageRequest.of(page, amountPerPage, Sort.by("title"));

        if (group == null) reminders = reminderRepository.findAllByUserAndGroupIsNull(user, pageable).getContent();
        else {
            groupService.checkUserIsGroupMemberVoid(user, group);
            reminders = reminderRepository.findAllByGroup(group, pageable).getContent();
        }

        return reminders.stream()
                .map(reminderMapper::toReminderListResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long create(@MayBeNull Long groupId, ReminderCreateRequest request) {
        if (request.sendAt().isBefore(LocalDateTime.now())) throw new DateTimeOperationException("Дата и время напоминания не могут быть в прошлом");

        User user = requireUserById(authService.getAuthenticatedUserId());
        Group group = groupId == null ? null : requireGroupById(groupId);

        Reminder reminder = Reminder.builder()
                .title(request.title())
                .message(request.message())
                .sendAt(request.sendAt())
                .status("Создано")
                .user(user)
                .group(group).build();

        return reminderRepository.save(reminder).getId();
    }

    private Reminder requireById(Long id) {
        return reminderRepository.findById(id).orElseThrow(() -> new ReminderNotFoundException("Напоминание с таким id не найдено"));
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }

    private void checkUserIsReminderOwner(Reminder reminder, User user) {
        if (!Objects.equals(reminder.getUser().getId(), user.getId())) throw new AccessDeniedException("Доступ к личному напоминанию имеет только ее владелец");
    }

    private void checkAccessToReminderGranted(Reminder reminder, User user) {
        Group group = reminder.getGroup();
        if (group == null) checkUserIsReminderOwner(reminder, user);
        else groupService.checkUserIsGroupMemberVoid(user, group);
    }
}
