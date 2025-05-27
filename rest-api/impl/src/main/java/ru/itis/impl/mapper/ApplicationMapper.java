package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.User;

@Component
public class ApplicationMapper {

    public ApplicationWithUserInfoResponse toApplicationWithUserInfoResponse(Application application) {
        User user = application.getUser();

        return ApplicationWithUserInfoResponse.builder()
                .applicationId(application.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .sendAt(application.getSendAt())
                .build();
    }
}
