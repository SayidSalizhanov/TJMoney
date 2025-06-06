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
import ru.itis.impl.service.*;

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
    private final Cloudinary cloudinary;
    private final AvatarRepository avatarRepository;
    private final TransactionService transactionService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    private final UserGroupRequireService userGroupRequireService;

    @Override
    @Transactional(readOnly = true)
    public UserSettingsResponse getInfo() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        return userMapper.toUserSettingsResponse(user);
    }

    @Override
    @Transactional
    public void updateInfo(UserSettingsRequest request) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        user.setUsername(request.username());
        user.setTelegramId(request.telegramId());
        user.setSendingToEmail(request.sendingToEmail());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserGroupResponse> getGroups() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

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
    public List<ApplicationToGroupResponse> getApplications() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

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
    public void deleteApplication(Long applicationId) {
        Application application = applicationService.requireById(applicationId);
        if (!application.getUser().getId().equals(authService.getAuthenticatedUserId())) {
            throw new AccessDeniedException("Вы не можете удалить эту заявку");
        }
        applicationService.delete(applicationId);
    }

    @Override
    @Transactional
    public void changePassword(UserPasswordChangeRequest request) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        String currentPassword = user.getPassword();
        String encodeNewPassword = passwordEncoder.encode(request.newPassword());

        if (!currentPassword.equals(encodeNewPassword)) throw new UpdateException("Неверный старый пароль");

        if (!request.newPassword().equals(request.repeatNewPassword())) throw new UpdateException("Новые пароли не совпадают");

        user.setPassword(encodeNewPassword);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AvatarResponse getAvatarUrl() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());
        return AvatarResponse.builder().url(user.getAvatar().getUrl()).build();
    }

    @Override
    @Transactional
    public void changeAvatar(MultipartFile avatarImage) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

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
    public void deleteAvatar() {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        avatarRepository.update("/images/defaultAvatar.png", user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfileInfo(String period) {
        User user = userGroupRequireService.requireUserById(authService.getAuthenticatedUserId());

        List<Map<String, Integer>> transactionsGenerals;
        if (period == null || period.isEmpty()) transactionsGenerals = transactionService.getUserTransactionsGenerals(user.getId(), "all");
        else transactionsGenerals = transactionService.getUserTransactionsGenerals(user.getId(), period);

        return userMapper.toUserProfileResponse(user, transactionsGenerals);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getStrangerProfileInfo(Long id, String period) {
        User user = userGroupRequireService.requireUserById(id);

        if (!authService.getAuthenticatedUserId().equals(user.getId())) return userMapper.toUserProfileResponse(user, null);

        List<Map<String, Integer>> transactionsGenerals;
        if (period == null || period.isEmpty()) transactionsGenerals = transactionService.getUserTransactionsGenerals(user.getId(), "all");
        else transactionsGenerals = transactionService.getUserTransactionsGenerals(user.getId(), period);

        return userMapper.toUserProfileResponse(user, transactionsGenerals);
    }
}