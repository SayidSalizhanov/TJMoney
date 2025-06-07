package ru.itis.impl.service;

import org.springframework.data.domain.Pageable;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.User;

import java.util.List;

public interface ApplicationService {
    Long createApplications(Long groupId);
    List<Application> getGroupApplicationsByGroupIdAndByStatus(Long groupId, String status, Pageable pageable);
    void updateStatus(Long applicationId, String status);
    void delete(Long applicationId);
    Application requireById(Long id);
}
