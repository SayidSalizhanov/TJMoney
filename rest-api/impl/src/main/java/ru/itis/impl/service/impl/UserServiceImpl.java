package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.user.UserSettingsResponse;
import ru.itis.impl.exception.UpdateException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.UserMapper;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.AvatarRepository;
import ru.itis.impl.repository.GroupMemberRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final AvatarRepository avatarRepository;

    @Override
    public UserSettingsResponse getUserSettingsInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));

        return userMapper.toUserSettingsResponse(user);
    }

    @Override
    public void updateUserSettingsInfo(Long id, UserSettingsRequest request) {
        User oldUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
        if (userRepository.findByUsername(request.username()).isPresent() && !oldUser.getUsername().equals(request.username())) throw new UpdateException("Пользователь с таким именем уже существует");

        userRepository.update(
                request.username(),
                request.telegramId().isEmpty() ? null : request.telegramId(),
                request.sendingToTelegram(),
                request.sendingToEmail(),
                id
        );
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));

        List<GroupMember> groupMembers = groupMemberRepository.findByUserAndRole(user, "ADMIN");
        for (GroupMember groupMember : groupMembers) {
            groupRepository.deleteById(groupMember.getGroup().getId());
        }
        userRepository.deleteById(id);
    }

    @Override
    public String getUserAvatarUrl(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));

        String avatarUrl = avatarRepository.findUrl(user);
        if (avatarUrl == null) return "defaultAvatar";
        return avatarUrl;
    }
}