package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.response.group.GroupMemberResponse;
import ru.itis.impl.model.GroupMember;
import ru.itis.impl.model.User;

@Component
public class GroupMemberMapper {

    public GroupMemberResponse toGroupMemberResponse(GroupMember groupMember) {
        User user = groupMember.getUser();

        return GroupMemberResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .joinedAt(groupMember.getJoinedAt())
                .role(groupMember.getRole())
                .build();
    }
}
