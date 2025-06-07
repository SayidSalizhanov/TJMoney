package ru.itis.impl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupMemberStatusEnum {
    ADMIN("ADMIN"),
    MEMBER("MEMBER");

    private final String value;
}
