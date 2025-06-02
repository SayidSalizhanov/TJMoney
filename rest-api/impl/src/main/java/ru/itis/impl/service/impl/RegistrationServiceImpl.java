package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.dto.request.user.UserRegisterRequest;
import ru.itis.impl.exception.RegistrationServiceException;
import ru.itis.impl.model.Avatar;
import ru.itis.impl.model.ERole;
import ru.itis.impl.model.Role;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.AvatarRepository;
import ru.itis.impl.repository.RoleRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.RegistrationService;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Long register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RegistrationServiceException("User with this email already exists", HttpStatus.CONFLICT);
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new RegistrationServiceException("Password and repeat password non equals", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .sendingToTelegram(false)
                .sendingToEmail(false)
                .build();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RegistrationServiceException("Error: Role is not found.", HttpStatus.NOT_FOUND));

        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        avatarRepository.save(Avatar.builder()
                .url("/defaultAvatar.png")
                .user(user)
                .build());

        return savedUser.getId();
    }
}
