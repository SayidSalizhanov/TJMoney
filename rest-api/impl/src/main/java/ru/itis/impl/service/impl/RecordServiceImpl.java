package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;
import ru.itis.impl.annotations.MayBeNull;
import ru.itis.impl.exception.AccessDeniedException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.RecordNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.RecordMapper;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.Record;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.RecordRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.GroupService;
import ru.itis.impl.service.RecordService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    @Override
    @Transactional(readOnly = true)
    public RecordSettingsResponse getById(Long id, Long userId) {
        User user = requireUserById(userId);
        Record record = requireById(id);

        checkAccessToRecordGranted(record, user);

        return recordMapper.toRecordSettingsResponse(record);
    }

    @Override
    @Transactional
    public void updateInfo(Long id, Long userId, RecordSettingsRequest request) {
        User user = requireUserById(userId);
        Record record = requireById(id);

        checkAccessToRecordGranted(record, user);

        record.setContent(request.content());
        record.setTitle(request.title());
        record.setUpdatedAt(LocalDateTime.now());
        recordRepository.save(record);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        User user = requireUserById(userId);
        Record record = requireById(id);

        checkAccessToRecordGranted(record, user);

        recordRepository.delete(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecordListResponse> getAll(Long userId, @MayBeNull Long groupId, Integer page, Integer amountPerPage, String sort) {
        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

        List<Record> records;

        if (group == null) records = recordRepository.findByUser(user);
        else {
            groupService.checkUserIsGroupMemberVoid(user, group);
            records = recordRepository.findByGroup(group);
        }

        return records.stream()
                .map(recordMapper::toRecordListResponse)
                .toList();
    }

    @Override
    @Transactional
    public Long create(Long userId, @MayBeNull Long groupId, RecordCreateRequest request) {
        User user = requireUserById(userId);
        Group group = groupId == null ? null : requireGroupById(groupId);

        Record record = Record.builder()
                .title(request.title())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .group(group)
                .build();

        return recordRepository.save(record).getId();
    }

    private Record requireById(Long id) {
        return recordRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Запись с таким id не найдена"));
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }

    private void checkUserIsRecordOwner(Record record, User user) {
        if (!Objects.equals(record.getUser().getId(), user.getId())) throw new AccessDeniedException("Доступ к личной записи имеет только ее владелец");
    }

    private void checkAccessToRecordGranted(Record record, User user) {
        Group group = record.getGroup();
        if (group == null) checkUserIsRecordOwner(record, user);
        else groupService.checkUserIsGroupMemberVoid(user, group);
    }
}
