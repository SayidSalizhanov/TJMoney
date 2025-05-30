package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.api.RecordApi;
import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;
import ru.itis.impl.service.RecordService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecordController implements RecordApi {

    private final RecordService recordService;

    @Override
    public RecordSettingsResponse getRecord(Long id, Long userId) {
        return recordService.getById(id, userId);
    }

    @Override
    public void updateRecordInfo(Long id, Long userId, RecordSettingsRequest request) {
        recordService.updateInfo(id, userId, request);
    }

    @Override
    public void deleteRecord(Long id, Long userId) {
        recordService.delete(id, userId);
    }

    @Override
    public List<RecordListResponse> getRecords(Long userId, Long groupId, Integer page, Integer amountPerPage) {
        return recordService.getAll(userId, groupId, page, amountPerPage);
    }

    @Override
    public Long createRecord(Long userId, Long groupId, RecordCreateRequest request) {
        return recordService.create(userId, groupId, request);
    }
}
