package ru.itis.impl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.impl.model.ChatMessageEntity;

public interface ChatMessagesRepository extends JpaRepository<ChatMessageEntity, Long> {
}
