package ru.itis.impl.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.dto.request.goal.GoalCreateRequest;
import ru.itis.dto.request.goal.GoalSettingsRequest;
import ru.itis.dto.response.exception.ExceptionMessage;
import ru.itis.dto.response.goal.GoalListResponse;
import ru.itis.dto.response.goal.GoalSettingsResponse;
import ru.itis.impl.config.TestRestTemplateConfig;
import ru.itis.impl.mapper.GoalMapper;
import ru.itis.impl.model.Goal;
import ru.itis.impl.model.User;
import ru.itis.impl.repository.GoalRepository;
import ru.itis.impl.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestRestTemplateConfig.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@Sql(
        scripts = {"/db/dml/insert-test-users.sql", "/db/dml/insert-test-goals.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
public class GoalControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    GoalRepository goalRepository;
    @Autowired
    GoalMapper goalMapper;
    @Autowired
    UserRepository userRepository;

    @Test
    void testGetGoals() throws Exception {
        //given
        long userId = 1;

        //when
        ResponseEntity<List<GoalListResponse>> response = testRestTemplate.exchange(
                "/goals?userId={userId}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {},
                userId
        );

        //then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = userRepository.findById(userId).orElseThrow();
        Pageable pageable = PageRequest.of(0,10, Sort.by("title"));
        List<GoalListResponse> expectedResult = goalRepository.findAllByUserAndGroupIsNull(user, pageable).stream()
                .map(goalMapper::toGoalListResponse)
                .toList();

        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void testGetGoals_invalidUserId_shouldThrowUserNotFoundException() {
        //given
        long userId = 2;

        //when
        ResponseEntity<ExceptionMessage> response = testRestTemplate.exchange(
                "/goals?userId={userId}",
                HttpMethod.GET,
                null,
                ExceptionMessage.class,
                userId
        );

        //then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateGoal() throws Exception {
        //given
        long userId = 1;

        GoalCreateRequest request = GoalCreateRequest.builder()
                .title("lets go")
                .description("description")
                .progress(100)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GoalCreateRequest> entity = new HttpEntity<>(request, headers);

        //when
        ResponseEntity<Long> response = testRestTemplate.exchange(
                "/goals?userId={userId}",
                HttpMethod.POST,
                entity,
                Long.class,
                userId
        );

        //then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        long id = response.getBody();

        Goal goal = goalRepository.findById(id).orElseThrow();
        assertEquals(goal.getDescription(), request.description());
        assertEquals(goal.getTitle(), request.title());
        assertEquals(goal.getProgress(), request.progress());
    }

    @Test
    void testGetGoalById() throws Exception {
        //given
        long id = 1;

        //when
        ResponseEntity<GoalSettingsResponse> response = testRestTemplate.exchange(
                "/goals/{id}",
                HttpMethod.GET,
                null,
                GoalSettingsResponse.class,
                id
        );

        //then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        GoalSettingsResponse expectedResult = goalMapper.toGoalSettingsResponse(
                goalRepository.findById(id).orElseThrow()
        );
        assertEquals(expectedResult, response.getBody());
    }

    @Test
    void testGetGoalById_invalidId_shouldThrowGoalNotFoundException() {
        //given
        long id = 15;

        //when
        ResponseEntity<ExceptionMessage> response = testRestTemplate.exchange(
                "/goals/{id}",
                HttpMethod.GET,
                null,
                ExceptionMessage.class,
                id
        );

        //then
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateGoal() throws Exception {
        //given
        long id = 5;

        GoalSettingsRequest request = GoalSettingsRequest.builder()
                .title("title")
                .description("djsbhkj")
                .progress(10)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GoalSettingsRequest> entity = new HttpEntity<>(request, headers);

        //when
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/goals/{id}",
                HttpMethod.PUT,
                entity,
                Void.class,
                id
        );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Goal goal = goalRepository.findById(id).orElseThrow();
        assertEquals(goal.getDescription(), request.description());
        assertEquals(goal.getTitle(), request.title());
        assertEquals(goal.getProgress(), request.progress());
    }

    @Test
    void testDeleteGoal() throws Exception {
        //given
        long id = 1;

        //when
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/goals/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                id
        );

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(goalRepository.findById(id).isEmpty());
    }
}

