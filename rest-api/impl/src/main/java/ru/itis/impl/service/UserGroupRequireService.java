package ru.itis.impl.service;

import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;

public interface UserGroupRequireService {
    User requireUserById(Long userId);
    Group requireGroupById(Long groupId);
}
