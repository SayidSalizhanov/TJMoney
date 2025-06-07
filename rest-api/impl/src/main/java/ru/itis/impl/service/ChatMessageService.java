package ru.itis.impl.service;

import ru.itis.dto.socket.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    Long save(ChatMessage chatMessage);
    List<ChatMessage> findLastMessagesByGroupId(Long groupId, int limit);
}
