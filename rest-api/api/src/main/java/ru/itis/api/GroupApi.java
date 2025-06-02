package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.application.ApplicationAnswerRequest;
import ru.itis.dto.request.group.GroupCreateRequest;
import ru.itis.dto.request.group.GroupSettingsRequest;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.dto.response.group.*;

import java.util.List;

@RequestMapping("/api/groups")
public interface GroupApi {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<GroupListResponse> getGroupsWhereUserNotJoined();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long createApplicationToGroup(
            @RequestParam("groupId") Long groupId
    );

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GroupProfileResponse getGroup(
            @PathVariable("id") Long id,
            @RequestParam(value = "period", required = false, defaultValue = "allTime") String period
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void leaveGroup(
            @PathVariable("id") Long id
    );

    @GetMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    GroupSettingsResponse getGroupSettings(
            @PathVariable("id") Long id
    );

    @PutMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    void updateGroupInfo(
            @PathVariable("id") Long id,
            @RequestBody GroupSettingsRequest request
    );

    @DeleteMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGroup(
            @PathVariable("id") Long id
    );

    @GetMapping("/{id}/viewing")
    @ResponseStatus(HttpStatus.OK)
    GroupViewingResponse getGroupView(
            @PathVariable("id") Long id
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createGroup(@RequestBody GroupCreateRequest request);

    @GetMapping("/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    List<GroupMemberResponse> getGroupMembers(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @DeleteMapping("/{id}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMemberFromAdminSide(
            @PathVariable("id") Long id,
            @RequestParam("userIdForDelete") Long userIdForDelete
    );

    @GetMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    List<ApplicationWithUserInfoResponse> getApplications(
            @PathVariable("id") Long id,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage
    );

    @PostMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    Long answerApplication(
            @PathVariable("id") Long id,
            @RequestBody ApplicationAnswerRequest request
    );
}
