package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.impl.model.Application;
import ru.itis.impl.model.User;

import java.time.format.DateTimeFormatter;

@Component
public class ApplicationMapper {

    public ApplicationWithUserInfoResponse toApplicationWithUserInfoResponse(Application application) {
        User user = application.getUser();

        return ApplicationWithUserInfoResponse.builder()
                .applicationId(application.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .sendAt(application.getSendAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
