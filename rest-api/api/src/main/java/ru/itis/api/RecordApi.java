package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.record.RecordCreateRequest;
import ru.itis.dto.request.record.RecordSettingsRequest;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;

import java.util.List;

@RequestMapping("/records")
public interface RecordApi {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    RecordSettingsResponse getRecord(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void updateRecordInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody RecordSettingsRequest request
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteRecord(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<RecordListResponse> getRecords(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "title") String sort
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createRecord(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "groupId", required = false, defaultValue = "null") Long groupId,
            @RequestBody RecordCreateRequest request
    );
}
