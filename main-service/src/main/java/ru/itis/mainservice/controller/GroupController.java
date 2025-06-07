package ru.itis.mainservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.mainservice.dto.request.application.ApplicationAnswerRequest;
import ru.itis.mainservice.dto.request.group.GroupCreateRequest;
import ru.itis.mainservice.dto.request.group.GroupSettingsRequest;
import ru.itis.mainservice.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.mainservice.dto.response.group.*;
import ru.itis.mainservice.service.AuthService;
import ru.itis.mainservice.service.GroupService;

import java.util.List;

@Controller
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final AuthService authService;

    @GetMapping
    public String getGroupsWhereUserNotJoined(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<GroupListResponse> groups = groupService.getGroupsWhereUserNotJoined(page, amountPerPage);
        model.addAttribute("groups", groups);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);
        return "group/groups";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createApplicationToGroup(@RequestParam Long groupId) {
        groupService.createApplicationToGroup(groupId);
    }

    @GetMapping("/{id}")
    public String getGroup(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "all") String period,
            Model model
    ) {
        GroupProfileResponse group = groupService.getGroup(id, period);
        model.addAttribute("group", group);
        return "group/group";
    }

    @DeleteMapping("/{id}")
    public String leaveGroup(@PathVariable Long id) {
        groupService.leaveGroup(id);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/settings")
    public String getGroupSettings(
            @PathVariable Long id,
            Model model
    ) {
        GroupSettingsResponse settings = groupService.getGroupSettings(id);
        model.addAttribute("settings", settings);
        return "group/settings";
    }

    @PutMapping("/{id}/settings")
    public String updateGroupInfo(
            @PathVariable Long id,
            @Valid GroupSettingsRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");

            GroupSettingsResponse settings = groupService.getGroupSettings(id);
            model.addAttribute("settings", settings);
            model.addAttribute("error", errorMessage);
            return "group/settings";
        }

        groupService.updateGroupInfo(id, request);
        return "redirect:/groups/" + id;
    }

    @DeleteMapping("/{id}/settings")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/viewing")
    public String getGroupView(
            @PathVariable Long id,
            Model model
    ) {
        GroupViewingResponse group = groupService.getGroupView(id);
        model.addAttribute("group", group);
        return "group/viewing";
    }

    @GetMapping("/new")
    public String getNewGroupPage(Model model) {
        return "group/new";
    }

    @PostMapping("/new")
    public String createGroup(@Valid GroupCreateRequest request,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Ошибка валидации");
            model.addAttribute("error", errorMessage);
            return "group/new";
        }

        Long groupId = groupService.createGroup(request);
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/{id}/members")
    public String getGroupMembers(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<GroupMemberResponse> members = groupService.getGroupMembers(id, page, amountPerPage);
        model.addAttribute("members", members);
        model.addAttribute("groupId", id);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);

        for (GroupMemberResponse member : members) {
            if (member.userId().equals(authService.getAuthenticatedUserId())) {
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
            @RequestParam Long userIdForDelete
    ) {
        groupService.deleteMemberFromAdminSide(id, userIdForDelete);
    }

    @GetMapping("/{id}/applications")
    public String getApplications(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            Model model
    ) {
        List<ApplicationWithUserInfoResponse> applications = groupService.getApplications(id, page, amountPerPage);
        model.addAttribute("applications", applications);
        model.addAttribute("groupId", id);
        model.addAttribute("page", page);
        model.addAttribute("amountPerPage", amountPerPage);
        return "group/applications";
    }

    @PostMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    public void answerApplication(
            @PathVariable Long id,
            ApplicationAnswerRequest request
    ) {
        groupService.answerApplication(id, request);
    }
} 