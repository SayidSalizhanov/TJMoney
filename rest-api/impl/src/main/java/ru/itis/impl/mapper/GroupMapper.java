package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.group.GroupListResponse;
import ru.itis.dto.response.group.GroupProfileResponse;
import ru.itis.dto.response.group.GroupSettingsResponse;
import ru.itis.dto.response.group.GroupViewingResponse;
import ru.itis.impl.model.Group;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class GroupMapper {

    public GroupListResponse toGroupListResponse(Group group) {
        return GroupListResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .build();
    }

    public GroupProfileResponse toGroupProfileResponse(List<Map<String, Integer>> transactionsGeneral, String userRole, Group group) {
        return GroupProfileResponse.builder()
                .transactionsGenerals(transactionsGeneral)
                .userRole(userRole)
                .id(group.getId())
                .name(group.getName())
                .createdAt(group.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(group.getDescription())
                .build();
    }

    public GroupSettingsResponse toGroupSettingsResponse(Group group, String userRole) {
        return GroupSettingsResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .userRole(userRole)
                .build();
    }

    public GroupViewingResponse toGroupViewingResponse(Group group, Integer membersCount, String adminName) {
        return GroupViewingResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .createdAt(group.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(group.getDescription())
                .membersCount(membersCount)
                .adminName(adminName)
                .build();
    }
}
