package ru.itis.impl.service;

import ru.itis.impl.model.Application;

import java.util.List;

public interface ApplicationService {
    Long createApplications(Long userId, Long groupId);
    List<Application> getGroupApplicationsByGroupIdAndByStatus(Long groupId, String status);
    void updateStatus(Long applicationId, String status);
}
