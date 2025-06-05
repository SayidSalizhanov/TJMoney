package ru.itis.impl.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.socket.ChatMessage;
import ru.itis.impl.exception.not_found.GroupNotFoundException;
import ru.itis.impl.exception.not_found.UserNotFoundException;
import ru.itis.impl.mapper.ChatMessageMapper;
import ru.itis.impl.model.ChatMessageEntity;
import ru.itis.impl.model.Group;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.ChatMessagesRepository;
import ru.itis.impl.repository.GroupRepository;
import ru.itis.impl.repository.UserRepository;
import ru.itis.impl.service.AuthService;
import ru.itis.impl.service.ChatMessageService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessagesRepository chatMessagesRepository;
    private final ChatMessageMapper chatMessageMapper;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Long save(ChatMessage chatMessage) {
        Long userId = authService.getAuthenticatedUserId();

        ChatMessageEntity entity = ChatMessageEntity.builder()
                .content(chatMessage.getContent())
                .type(chatMessage.getType())
                .datetime(LocalDateTime.now())
                .user(requireUserById(userId))
                .group(requireGroupById(Long.valueOf(chatMessage.getGroupId())))
                .build();

        return chatMessagesRepository.save(entity).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> findLastMessagesByGroupId(Long groupId, int limit) {
        log.info("Getting last {} messages for group {}", limit, groupId);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ChatMessageEntity> cq = cb.createQuery(ChatMessageEntity.class);
        Root<ChatMessageEntity> root = cq.from(ChatMessageEntity.class);

        cq.select(root)
                .where(cb.equal(root.get("group").get("id"), groupId))
                .orderBy(cb.asc(root.get("datetime")));

        return entityManager.createQuery(cq)
                .setMaxResults(limit)
                .getResultList().stream()
                .map(chatMessageMapper::toChatMessage)
                .toList();
    }

    private User requireUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с таким id не найден"));
    }

    private Group requireGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("Группа с таким id не найдена"));
    }
}
