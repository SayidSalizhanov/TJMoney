package ru.itis.impl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.socket.ChatMessage;
import ru.itis.dto.socket.MessageType;
import ru.itis.impl.service.ChatMessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final Map<String, String> userSessions = new ConcurrentHashMap<>();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/group/{groupId}/messages")
    public ResponseEntity<List<ChatMessage>> getLastMessages(
            @PathVariable String groupId,
            @RequestParam(defaultValue = "30") int limit) {
        List<ChatMessage> lastMessages = chatMessageService.findLastMessagesByGroupId(Long.valueOf(groupId), limit);
        return ResponseEntity.ok(lastMessages);
    }

    @PostMapping("/group/{groupId}/send")
    public ResponseEntity<Void> sendGroupMessage(
            @PathVariable String groupId,
            @RequestBody ChatMessage message) {
        log.info("Received message from user {} in group {}: {}", 
            message.getSender(), groupId, message.getContent());
        
        ChatMessage broadcastMessage = ChatMessage.builder()
            .sender(message.getSender())
            .content(message.getContent())
            .type(MessageType.CHAT)
            .timestamp(LocalDateTime.now().format(TIME_FORMATTER))
            .groupId(groupId)
            .build();
        
        chatMessageService.save(broadcastMessage);
        
        String destination = "/topic/group/" + groupId;
        log.debug("Broadcasting message to group {}: {}", groupId, broadcastMessage);
        messagingTemplate.convertAndSend(destination, broadcastMessage);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/group/{groupId}/join")
    public ResponseEntity<Void> joinGroup(
            @PathVariable String groupId,
            @RequestBody ChatMessage message) {
        log.info("User {} joining group {}", message.getSender(), groupId);
        
        userSessions.put(message.getSender(), groupId);
        
        ChatMessage broadcastMessage = ChatMessage.builder()
            .sender(message.getSender())
            .content(message.getContent())
            .type(MessageType.JOIN)
            .timestamp(LocalDateTime.now().format(TIME_FORMATTER))
            .groupId(groupId)
            .build();
        
        chatMessageService.save(broadcastMessage);
        
        String destination = "/topic/group/" + groupId;
        log.debug("Broadcasting join message to group {}: {}", groupId, broadcastMessage);
        messagingTemplate.convertAndSend(destination, broadcastMessage);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/group/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable String groupId,
            @RequestBody ChatMessage message) {
        log.info("User {} leaving group {}", message.getSender(), groupId);
        
        userSessions.remove(message.getSender());
        
        ChatMessage broadcastMessage = ChatMessage.builder()
            .sender(message.getSender())
            .content(message.getContent())
            .type(MessageType.LEAVE)
            .timestamp(LocalDateTime.now().format(TIME_FORMATTER))
            .groupId(groupId)
            .build();
        
        chatMessageService.save(broadcastMessage);
        
        String destination = "/topic/group/" + groupId;
        log.debug("Broadcasting leave message to group {}: {}", groupId, broadcastMessage);
        messagingTemplate.convertAndSend(destination, broadcastMessage);
        
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        log.info("Received WebSocket message: {}", chatMessage);
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, 
                       SimpMessageHeaderAccessor headerAccessor) {
        log.info("User {} joined chat", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        messagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
} 