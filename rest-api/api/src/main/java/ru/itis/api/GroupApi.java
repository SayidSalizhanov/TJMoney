package ru.itis.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itis.dto.request.application.ApplicationAnswerRequest;
import ru.itis.dto.request.group.GroupCreateRequest;
import ru.itis.dto.request.group.GroupSettingsRequest;
import ru.itis.dto.response.application.ApplicationWithUserInfoResponse;
import ru.itis.dto.response.group.*;

import java.util.List;

@RequestMapping("/groups")
public interface GroupApi {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<GroupListResponse> getGroupsWhereUserNotJoined(
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Long createApplicationToGroup(
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam("groupId") Long groupId
    );

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    GroupProfileResponse getGroup(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "period", required = false, defaultValue = "allTime") String period
    );

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void leaveGroup(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    GroupSettingsResponse getGroupSettings(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PutMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.OK)
    void updateGroupInfo(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody GroupSettingsRequest request
    );

    @DeleteMapping("/{id}/settings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteGroup(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @GetMapping("/{id}/viewing")
    @ResponseStatus(HttpStatus.OK)
    GroupViewingResponse getGroupView(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId // todo get from authentication
    );

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    Long createGroup(
            @RequestParam("userId") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody GroupCreateRequest request
    );

    @GetMapping("/{id}/members")
    @ResponseStatus(HttpStatus.OK)
    List<GroupMemberResponse> getGroupMembers(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "username") String sort
    );

    @DeleteMapping("/{id}/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMember(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody String usernameForDelete
    );

    @GetMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.OK)
    List<ApplicationWithUserInfoResponse> getApplications(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "amount_per_page", required = false, defaultValue = "10") Integer amountPerPage,
            @RequestParam(value = "sort", required = false, defaultValue = "sendAt") String sort
    );

    @PostMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    Long answerApplication(
            @PathVariable("id") Long id,
            @RequestParam("userId") Long userId, // todo get from authentication
            @RequestBody ApplicationAnswerRequest request
    );
}
