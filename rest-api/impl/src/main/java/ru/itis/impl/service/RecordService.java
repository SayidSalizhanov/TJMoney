package ru.itis.impl.service;

import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;

import java.util.List;

public interface RecordService {
    RecordSettingsResponse getById(Long id);
    void updateInfo(Long id, RecordSettingsRequest request);
    void delete(Long id);
    List<RecordListResponse> getAll(Long groupId, Integer page, Integer amountPerPage);
    Long create(Long groupId, RecordCreateRequest request);
}
