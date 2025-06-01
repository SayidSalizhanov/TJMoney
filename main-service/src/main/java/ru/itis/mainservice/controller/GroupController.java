package ru.itis.mainservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.application.ApplicationAnswerRequest;
import ru.itis.mainservice.dto.request.group.GroupCreateRequest;
import ru.itis.mainservice.dto.request.group.GroupSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.mainservice.dto.response.group.*;
import ru.itis.mainservice.service.GroupService;

import java.util.List;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public String getGroupsWhereUserNotJoined(
            @RequestParam Long userId,
            Model model
    ) {
        List<GroupListResponse> groups = groupService.getGroupsWhereUserNotJoined(userId);
        model.addAttribute("groups", groups);
        model.addAttribute("userId", userId);
        return "group/groups";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createApplicationToGroup(
            @RequestParam Long userId,
            @RequestParam Long groupId
    ) {
        groupService.createApplicationToGroup(userId, groupId);
    }

    @GetMapping("/{id}")
    public String getGroup(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "all") String period,
            Model model
    ) {
        GroupProfileResponse group = groupService.getGroup(id, userId, period);
        model.addAttribute("group", group);
        model.addAttribute("userId", userId);
        return "group/group";
    }

    @DeleteMapping("/{id}")
    public String leaveGroup(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        groupService.leaveGroup(id, userId);
        return "redirect:/groups?userId=" + userId;
    }

    @GetMapping("/{id}/settings")
    public String getGroupSettings(
            @PathVariable Long id,
            @RequestParam Long userId,
            Model model
    ) {
        GroupSettingsResponse settings = groupService.getGroupSettings(id, userId);
        model.addAttribute("settings", settings);
        model.addAttribute("userId", userId);
        return "group/settings";
    }

    @PutMapping("/{id}/settings")
    public String updateGroupInfo(
            @PathVariable Long id,
            @RequestParam Long userId,
            GroupSettingsRequest request
    ) {
        groupService.updateGroupInfo(id, userId, request);
        return "redirect:/groups/" + id + "?userId=" + userId;
    }

    @DeleteMapping("/{id}/settings")
    public String deleteGroup(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        groupService.deleteGroup(id, userId);
        return "redirect:/groups?userId=" + userId;
    }

    @GetMapping("/{id}/viewing")
    public String getGroupView(
            @PathVariable Long id,
            @RequestParam Long userId,
            Model model
    ) {
        GroupViewingResponse group = groupService.getGroupView(id, userId);
        model.addAttribute("group", group);
        model.addAttribute("userId", userId);
        return "group/viewing";
    }

    @GetMapping("/new")
    public String getNewGroupPage(
            @RequestParam Long userId,
            Model model
    ) {
        model.addAttribute("userId", userId);
        return "group/new";
    }

    @PostMapping("/new")
    public String createGroup(
            @RequestParam Long userId,
            GroupCreateRequest request
    ) {
        Long groupId = groupService.createGroup(userId, request);
        return "redirect:/groups/" + groupId + "?userId=" + userId;
    }

    @GetMapping("/{id}/members")
    public String getGroupMembers(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<GroupMemberResponse> members = groupService.getGroupMembers(id, userId, page, amountPerPage);
        model.addAttribute("members", members);
        model.addAttribute("groupId", id);
        model.addAttribute("userId", userId);

        for (GroupMemberResponse member : members) {
            if (member.userId().equals(userId)) {
                if (member.role().equalsIgnoreCase("ADMIN")) return "group/membersAdmin";
                else return "group/members";
            }
        }
        return "group/members";
    }

    @DeleteMapping("/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMemberFromAdminSide(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam Long userIdForDelete
    ) {
        groupService.deleteMemberFromAdminSide(id, userId, userIdForDelete);
    }

    @GetMapping("/{id}/applications")
    public String getApplications(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<ApplicationWithUserInfoResponse> applications = groupService.getApplications(id, userId, page, amountPerPage);
        model.addAttribute("applications", applications);
        model.addAttribute("userId", userId);
        model.addAttribute("groupId", id);
        return "group/applications";
    }

    @PostMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    public void answerApplication(
            @PathVariable Long id,
            @RequestParam Long userId,
            ApplicationAnswerRequest request
    ) {
        groupService.answerApplication(id, userId, request);
    }
} 