package ru.itis.impl.mapper;

import org.springframework.stereotype.Component;
import ru.itis.dto.socket.ChatMessage;
import ru.itis.impl.model.ChatMessageEntity;

import java.time.format.DateTimeFormatter;

@Component
public class ChatMessageMapper {

    public ChatMessage toChatMessage(ChatMessageEntity entity) {
        return ChatMessage.builder()
                .content(entity.getContent())
                .groupId(entity.getGroup().getId().toString())
                .sender(entity.getUser().getUsername())
                .type(entity.getType())
                .timestamp(entity.getDatetime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .build();
    }
}
