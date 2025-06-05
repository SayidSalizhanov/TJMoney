package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.itis.mainservice.service.AuthService;
import ru.itis.mainservice.service.GroupService;
import ru.itis.mainservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatPageController {

    private final UserService userService;
    private final GroupService groupService;

    @GetMapping("/groups/{groupId}/chat")
    public String chatPage(@PathVariable String groupId, Model model) {
        try {
            groupService.getGroup(Long.parseLong(groupId), "all");
            
            model.addAttribute("userService", userService);
            model.addAttribute("groupId", groupId);
            return "group/chat";
        } catch (HttpClientErrorException.Forbidden e) {
            log.warn("User tried to access chat of group {} but is not a member", groupId);
            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error accessing chat page", e);
            return "redirect:/login";
        }
    }
} 