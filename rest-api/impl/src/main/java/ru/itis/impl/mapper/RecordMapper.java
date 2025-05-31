package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.record.RecordListResponse;
import ru.itis.dto.response.record.RecordSettingsResponse;
import ru.itis.impl.model.Record;

import java.time.format.DateTimeFormatter;

@Component
public class RecordMapper {

    public RecordSettingsResponse toRecordSettingsResponse(Record record) {
        return RecordSettingsResponse.builder()
                .title(record.getTitle())
                .content(record.getContent())
                .createdAt(record.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedAt(record.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public RecordListResponse toRecordListResponse(Record record) {
        return RecordListResponse.builder()
                .id(record.getId())
                .title(record.getTitle())
                .build();
    }
}
