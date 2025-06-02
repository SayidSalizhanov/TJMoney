package ru.itis.impl.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dto.request.user.UserPasswordChangeRequest;
import ru.itis.dto.request.user.UserSettingsRequest;
import ru.itis.dto.response.application.ApplicationToGroupResponse;
import ru.itis.dto.response.avatar.AvatarResponse;
import ru.itis.dto.response.user.UserGroupResponse;
import ru.itis.dto.response.user.UserProfileResponse;
import ru.itis.dto.response.user.UserSettingsResponse;
import ru.itis.impl.exception.AccessDeniedException;
import ru.itis.impl.exception.FileProcessingException;
import ru.itis.impl.exception.UpdateException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.UserMapper;
import ru.itis.impl.model.*;
import ru.itis.impl.repository.AvatarRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.ApplicationService;
import ru.itis.impl.service.GroupMemberService;
import ru.itis.impl.service.TransactionService;
import ru.itis.impl.service.UserService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ApplicationService applicationService;
    private final GroupRepository groupRepository;
    private final Cloudinary cloudinary;
    private final AvatarRepository avatarRepository;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserSettingsResponse getInfo(Long id, Long userId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        return userMapper.toUserSettingsResponse(user);
    }

    @Override
    @Transactional
    public void updateInfo(Long id, Long userId, UserSettingsRequest request) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        user.setUsername(request.username());
        user.setTelegramId(request.telegramId());
        user.setSendingToTelegram(request.sendingToTelegram());
        user.setSendingToEmail(request.sendingToEmail());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id, Long userId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupResponse> getGroups(Long id, Long userId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        List<GroupMember> groupMembers = user.getGroupMembers();
        List<UserGroupResponse> responses = new ArrayList<>();

        for (GroupMember gm : groupMembers) {
            Group group = gm.getGroup();

            responses.add(UserGroupResponse.builder()
                    .groupId(group.getId())
                    .groupName(group.getName())
                    .description(group.getDescription())
                    .role(gm.getRole())
                    .build());
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationToGroupResponse> getApplications(Long id, Long userId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        List<Application> applications = user.getApplications();
        return applications.stream()
                .map(a -> ApplicationToGroupResponse.builder()
                        .applicationId(a.getId())
                        .groupName(a.getGroup().getName())
                        .status(a.getStatus())
                        .sendAt(a.getSendAt())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteApplication(Long id, Long userId, Long applicationId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        applicationService.delete(applicationId);
    }

    @Override
    @Transactional
    public void changePassword(Long id, Long userId, UserPasswordChangeRequest request) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        String currentPassword = user.getPassword();
        String encodeNewPassword = passwordEncoder.encode(request.newPassword());

        if (!currentPassword.equals(encodeNewPassword)) throw new UpdateException("Неверный старый пароль");

        if (!request.newPassword().equals(request.repeatNewPassword())) throw new UpdateException("Новые пароли не совпадают");

        user.setPassword(encodeNewPassword);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AvatarResponse getAvatarUrl(Long id, Long userId) {
        User user = requireUserById(id); // здесь не нужно проверки на доступ (userId бесполезен)
        return AvatarResponse.builder().url(user.getAvatar().getUrl()).build();
    }

    @Override
    @Transactional
    public void changeAvatar(Long id, Long userId, MultipartFile avatarImage) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        if (avatarImage != null && avatarImage.getSize() > 0) {
            try {
                File tempFile = new File(avatarImage.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(avatarImage.getBytes());
                }

                try {
                    Map uploadResult = cloudinary.uploader().upload(tempFile, Map.of());
                    String avatarUrl = (String) uploadResult.get("secure_url");

                    avatarRepository.update(avatarUrl, user);
                } catch (Exception e) {
                    throw new FileProcessingException("Не удалось загрузить аватар", HttpStatus.BAD_REQUEST);
                } finally {
                    tempFile.delete();
                }
            } catch (IOException e) {
                throw new FileProcessingException("Не удалось прочитать файл", HttpStatus.BAD_REQUEST);
            }
        }
        else throw new FileProcessingException("Файл пуст", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public void deleteAvatar(Long id, Long userId) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        checkUserHasAccessGranted(user, currentSessionUser);

        avatarRepository.update("/defaultAvatar.png", user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfileInfo(Long id, Long userId, String period) {
        User user = requireUserById(id);
        User currentSessionUser = requireUserById(userId);

        if (!user.getId().equals(currentSessionUser.getId())) return userMapper.toUserProfileResponse(user, null);

        List<Map<String, Integer>> transactionsGenerals;
        if (period == null || period.isEmpty()) transactionsGenerals = transactionService.getUserTransactionsGenerals(userId, "all");
        else transactionsGenerals = transactionService.getUserTransactionsGenerals(userId, period);

        return userMapper.toUserProfileResponse(user, transactionsGenerals);
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }

    private void checkUserHasAccessGranted(User user, User currentSessionUser) {
        if (!user.getId().equals(currentSessionUser.getId())) throw new AccessDeniedException("Доступ к этой странице запрещен");
    }
}