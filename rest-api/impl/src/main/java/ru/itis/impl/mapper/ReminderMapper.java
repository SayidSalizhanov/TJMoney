package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.reminder.ReminderListResponse;
import ru.itis.dto.response.reminder.ReminderSettingsResponse;
import ru.itis.impl.model.Reminder;

@Component
public class ReminderMapper {

    public ReminderSettingsResponse toReminderSettingsResponse(Reminder reminder) {
        return ReminderSettingsResponse.builder()
                .title(reminder.getTitle())
                .message(reminder.getMessage())
                .sendAt(reminder.getSendAt())
                .status(reminder.getStatus())
                .build();
    }

    public ReminderListResponse toReminderListResponse(Reminder reminder) {
        return ReminderListResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .status(reminder.getStatus())
                .build();
    }
}
