package ru.itis.impl.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.impl.exception.not_found.ApplicationNotFoundException;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.ApplicationRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.ApplicationService;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.UserGroupRequireService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.itis.impl.enums.ApplicationStatusEnum.PENDING;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final AuthService authService;

    private final UserGroupRequireService userGroupRequireService;

    @Override
    @Transactional
    public Long createApplications(Long groupId) {
        Application application = Application.builder()
                .user(userGroupRequireService.requireUserById(authService.getAuthenticatedUserId()))
                .group(userGroupRequireService.requireGroupById(groupId))
                .sendAt(LocalDateTime.now())
                .status(PENDING.getValue())
                .build();

        return applicationRepository.save(application).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Application> getGroupApplicationsByGroupIdAndByStatus(Long groupId, String status, Pageable pageable) {
        return applicationRepository.findAllByGroupAndStatus(userGroupRequireService.requireGroupById(groupId), status, pageable).getContent();
    }

    @Override
    @Transactional
    public void updateStatus(Long applicationId, String status) {
        Application application = requireById(applicationId);
        application.setStatus(status);
        applicationRepository.save(application);
    }

    @Override
    @Transactional
    public void delete(Long applicationId) {
        applicationRepository.deleteById(applicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Application requireById(Long id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ApplicationNotFoundException("Заявка с таким id не найдена"));
    }
}
