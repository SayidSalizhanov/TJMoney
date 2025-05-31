package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.group.GroupMemberResponse;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;

import java.time.format.DateTimeFormatter;

@Component
public class GroupMemberMapper {

    public GroupMemberResponse toGroupMemberResponse(GroupMember groupMember) {
        User user = groupMember.getUser();

        return GroupMemberResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .joinedAt(groupMember.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role(groupMember.getRole())
                .build();
    }
}
