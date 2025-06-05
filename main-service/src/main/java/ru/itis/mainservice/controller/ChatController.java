package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import ru.itis.mainservice.dto.socket.ChatMessage;
import ru.itis.mainservice.dto.socket.MessageType;
import ru.itis.mainservice.service.AuthService;
import ru.itis.mainservice.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/groups/{groupId}/chat")
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Value("${api.base-url}")
    private String apiBaseUrl;

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessage> getLastMessages(@PathVariable String groupId,
                                           @RequestParam(defaultValue = "30") int limit) {
        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                apiBaseUrl + "/api/chat/group/" + groupId + "/messages?limit=" + limit,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ChatMessage>>() {}
        ).getBody();
    }

    @PostMapping("/messages")
    @ResponseBody
    public void sendMessage(@PathVariable String groupId,
                          @RequestBody ChatMessage message) {
        String username = userService.getUserProfileInfo("all").username();
        logger.info("User {} sending message to group {}: {}", username, groupId, message.getContent());

        // Проверяем, что отправитель совпадает с текущим пользователем
        if (!username.equals(message.getSender())) {
            logger.warn("Message sender {} does not match current user {}", message.getSender(), username);
            return;
        }

        message.setType(MessageType.CHAT);
        message.setTimestamp(LocalDateTime.now().format(TIME_FORMATTER));

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<ChatMessage> entity = new HttpEntity<>(message, headers);

        // Send message to API
        restTemplate.exchange(
            apiBaseUrl + "/api/chat/group/" + groupId + "/send",
            HttpMethod.POST,
            entity,
            Void.class
        );
    }

    @PostMapping("/join")
    @ResponseBody
    public void joinGroup(@PathVariable String groupId) {
        String username = userService.getUserProfileInfo("all").username();
        logger.info("User {} joining group {}", username, groupId);
        
        ChatMessage joinMessage = new ChatMessage();
        joinMessage.setSender(username);
        joinMessage.setType(MessageType.JOIN);
        joinMessage.setContent(username + " joined the chat");
        joinMessage.setTimestamp(LocalDateTime.now().format(TIME_FORMATTER));

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<ChatMessage> entity = new HttpEntity<>(joinMessage, headers);
        
        // Send join message to API
        restTemplate.exchange(
            apiBaseUrl + "/api/chat/group/" + groupId + "/join",
            HttpMethod.POST,
            entity,
            Void.class
        );
    }

    @PostMapping("/leave")
    @ResponseBody
    public void leaveGroup(@PathVariable String groupId) {
        String username = userService.getUserProfileInfo("all").username();
        logger.info("User {} leaving group {}", username, groupId);
        
        ChatMessage leaveMessage = new ChatMessage();
        leaveMessage.setSender(username);
        leaveMessage.setType(MessageType.LEAVE);
        leaveMessage.setContent(username + " left the chat");
        leaveMessage.setTimestamp(LocalDateTime.now().format(TIME_FORMATTER));

        HttpHeaders headers = authService.getAuthHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<ChatMessage> entity = new HttpEntity<>(leaveMessage, headers);
        
        // Send leave message to API
        restTemplate.exchange(
            apiBaseUrl + "/api/chat/group/" + groupId + "/leave",
            HttpMethod.POST,
            entity,
            Void.class
        );
    }
} 