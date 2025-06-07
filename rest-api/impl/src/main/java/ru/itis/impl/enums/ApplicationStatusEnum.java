package ru.itis.impl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatusEnum {
    APPROVED("Одобрено"),
    REJECTED("Отклонено"),
    PENDING("В ожидании");

    private final String value;
}
