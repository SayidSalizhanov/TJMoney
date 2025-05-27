package ru.itis.impl.service;

import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;

import java.util.List;

public interface RecordService {
    RecordSettingsResponse getById(Long id, Long userId);
    void updateInfo(Long id, Long userId, RecordSettingsRequest request);
    void delete(Long id, Long userId);
    List<RecordListResponse> getAll(Long userId, Long groupId, Integer page, Integer amountPerPage, String sort);
    Long create(Long userId, Long groupId, RecordCreateRequest request);
}
