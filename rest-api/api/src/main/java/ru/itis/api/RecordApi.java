package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;

import java.util.List;

@RequestMapping("/api/records")
public interface RecordApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecordSettingsResponse getRecord(
            @PathVariable("id") Long id
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateRecordInfo(
            @PathVariable("id") Long id,
            @RequestBody RecordSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRecord(
            @PathVariable("id") Long id
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<RecordListResponse> getRecords(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createRecord(
            @RequestParam(value = "groupId", required = false) Long groupId,
            @RequestBody RecordCreateRequest request
    );
}
