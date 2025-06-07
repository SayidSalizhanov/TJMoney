package ru.itis.impl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReminderStatusEnum {
    CREATED("Создано"),
    SENT("Отправлено"),
    NOT_SENT("Не отправлено");

    private final String value;
}
