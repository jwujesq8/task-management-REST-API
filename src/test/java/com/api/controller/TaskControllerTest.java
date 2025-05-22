package com.api.controller;

import com.api.dto.*;
import com.api.dto.jwt.JwtRequestDto;
import com.api.dto.jwt.JwtResponseDto;
import com.api.repository.UserRepository;
import com.api.service.auth.AuthServiceImpl;
import com.api.service.interfaces.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository; // real users from the DB to check role permission
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AuthServiceImpl authService;
    @MockBean
    private TaskService taskService;

    private WebTestClient webTestClient;
    private UUID taskId;
    private UUID adminId;
    private UUID userId;
    private UUID nonExecutorId;
    private TaskDto taskDto;
    private UserDto userDto;
    private UserDto adminDto;
    private UserDto nonExecutorDto;

    record InvalidDtoCase(String name, Object dto) {
        @Override
        public String toString() {
            return name;
        }}

    String baseUrl() {
        return "http://localhost:" + port;
    }

    HttpHeaders getHeadersWithBearerAuth(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    HttpEntity<Object> getHttpEntity(Object body, String token){
        return new HttpEntity<>(body, getHeadersWithBearerAuth(token));
    }

    ResponseEntity<JwtResponseDto> login(String email, String password){
        return restTemplate.postForEntity(
                baseUrl() + "/auth/login",
                JwtRequestDto.builder()
                        .email(email)
                        .password(password)
                        .build(),
                JwtResponseDto.class);
    }

    @BeforeEach
    void setUp(){
        this.webTestClient = WebTestClient.bindToServer() // for PATCH requests
                .baseUrl(baseUrl())
                .build();
        adminId = UUID.fromString("ecf72b35-4151-4439-a5a1-408d2ce330c5");
        adminDto = modelMapper.map(userRepository.findById(adminId), UserDto.class);

        userId = UUID.fromString("a88589c6-0f3a-47fc-8a43-78f9f9bb78ff");
        userDto = modelMapper.map(userRepository.findById(userId), UserDto.class);

        nonExecutorId = UUID.fromString("892a0f4d-3615-43fd-b3d2-90171fac84df");
        nonExecutorDto = modelMapper.map(userRepository.findById(nonExecutorId), UserDto.class);

        taskId = UUID.randomUUID();
        taskDto = TaskDto.builder()
                .id(taskId)
                .title("Test task title")
                .description("Task description")
                .priority("mid")
                .status("in progress")
                .creator(adminDto)
                .executor(userDto)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        authService.getTokenStore().clear();
    }

    @Nested
    class addTask{

        private static Stream<Arguments> invalidPostTaskDtos() {
            return Stream.of(
                    Arguments.of(new InvalidDtoCase(
                            "title - null | creator - null | executor - null",
                            TaskNoIdDto.builder()
                                    .title(null)
                                    .description("description")
                                    .status("completed")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                            )),
                    Arguments.of(new InvalidDtoCase(
                            "title - empty | creator - null | executor - null",
                            TaskNoIdDto.builder()
                                    .title("")
                                    .description("description")
                                    .status("completed")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "status - out of patter (pending|in progress|completed) | creator - null | executor - null",
                            TaskNoIdDto.builder()
                                    .title("title")
                                    .description("description")
                                    .status("out of pattern")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "priority - out of patter (high|mid|low)| creator - null | executor - null",
                            TaskNoIdDto.builder()
                                    .title("title")
                                    .description("description")
                                    .status("completed")
                                    .priority("out of pattern")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    ))
            );
        }
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidPostTaskDtos")
        void invalidDto_shouldReturn400(InvalidDtoCase invalidDto){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(taskService.addTask(any(TaskNoIdDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    getHttpEntity(
                            invalidDto.dto(),
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.BAD_REQUEST, taskResponseEntity.getStatusCode());
        }

        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(taskService.addTask(any(TaskNoIdDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    getHttpEntity(
                            modelMapper.map(taskDto, TaskNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.CREATED, taskResponseEntity.getStatusCode());
            assertNotNull(taskResponseEntity.getBody());
            assertNotNull(taskResponseEntity.getBody().getId());
            assertEquals(taskResponseEntity.getBody().getCreator().getId(), adminDto.getId());
        }
        @Test
        void nonAdmin_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    getHttpEntity(
                            modelMapper.map(taskDto, TaskNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    modelMapper.map(taskDto, TaskNoIdDto.class),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class updateTask{

        private static Stream<Arguments> invalidPutTaskDtos() {
            return Stream.of(
                    Arguments.of(new InvalidDtoCase(
                            "id - null | creator - null | executor - null",
                            TaskDto.builder()
                                    .id(null)
                                    .title("title")
                                    .description("description")
                                    .status("completed")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "title - null | creator - null | executor - null",
                            TaskDto.builder()
                                    .id(UUID.randomUUID())
                                    .title(null)
                                    .description("description")
                                    .status("completed")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "title - empty | creator - null | executor - null",
                            TaskDto.builder()
                                    .id(UUID.randomUUID())
                                    .title("")
                                    .description("description")
                                    .status("completed")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "status - out of patter (pending|in progress|completed) | creator - null | executor - null",
                            TaskDto.builder()
                                    .id(UUID.randomUUID())
                                    .title("title")
                                    .description("description")
                                    .status("out of pattern")
                                    .priority("mid")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    )),
                    Arguments.of(new InvalidDtoCase(
                            "priority - out of patter (high|mid|low)| creator - null | executor - null",
                            TaskDto.builder()
                                    .id(UUID.randomUUID())
                                    .title("title")
                                    .description("description")
                                    .status("completed")
                                    .priority("out of pattern")
                                    .creator(null)
                                    .executor(null)
                                    .build()
                    ))
            );
        }
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidPutTaskDtos")
        void invalidDto_shouldReturn400(InvalidDtoCase invalidDto){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(taskService.updateTask(any(TaskDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.PUT,
                    getHttpEntity(
                            invalidDto.dto(),
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.BAD_REQUEST, taskResponseEntity.getStatusCode());
        }
        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(taskService.updateTask(any(TaskDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.PUT,
                    getHttpEntity(
                            taskDto,
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
            assertNotNull(taskResponseEntity.getBody());
            assertNotNull(taskResponseEntity.getBody().getId());
            assertEquals(taskResponseEntity.getBody().getCreator().getId(), adminDto.getId());
        }
        @Test
        void nonAdmin_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.PUT,
                    getHttpEntity(
                            taskDto,
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.PUT,
                    new HttpEntity<>(taskDto),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class updateTaskStatus{

        private static Stream<Arguments> invalidTaskStatusDtos() {
            return Stream.of(
                    Arguments.of(new InvalidDtoCase(
                            "name - null", new StatusDto(null))),
                    Arguments.of(new InvalidDtoCase(
                            "name - empty", new StatusDto(""))),
                    Arguments.of(new InvalidDtoCase(
                            "name - out of pattern (pending|in progress|completed)", new StatusDto("out of pattern")))
            );
        }
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidTaskStatusDtos")
        void invalidTaskStatusDto_shouldReturn400(InvalidDtoCase invalidDto){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());

            webTestClient.patch()
                    .uri("/tasks/{taskId}/status", taskId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtResponseEntity.getBody().getAccessToken())
                    .bodyValue(invalidDto.dto())
                    .exchange()
                    .expectStatus().isBadRequest();

        }

        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            taskDto.setStatus("completed");
            when(taskService.updateTaskStatus(any(UUID.class), any(String.class))).thenReturn(taskDto);

            webTestClient.patch()
                    .uri("/tasks/{taskId}/status", taskId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtResponseEntity.getBody().getAccessToken())
                    .bodyValue(new StatusDto("completed"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TaskDto.class)
                    .value(response -> {
                        assertEquals("completed", response.getStatus());
                        assertEquals(taskDto.getId(), response.getId());
                    });
        }
        @Test
        void nonAdminOrNonExecutor_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(nonExecutorDto.getEmail(), nonExecutorDto.getPassword());
            taskDto.setStatus("completed");

            webTestClient.patch()
                    .uri("/tasks/{taskId}/status", taskId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtResponseEntity.getBody().getAccessToken())
                    .bodyValue(new StatusDto("completed"))
                    .exchange()
                    .expectStatus().isForbidden();

        }
    }

    @Nested
    class deleteTask {
        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            doNothing().when(taskService).deleteTask(any(IdDto.class));

            ResponseEntity<Void> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.DELETE,
                    getHttpEntity(
                            new IdDto(taskId),
                            jwtResponseEntity.getBody().getAccessToken()),
                    Void.class);

            assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
        }
        @Test
        void nonAdmin_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());

            ResponseEntity<Void> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks",
                    HttpMethod.DELETE,
                    getHttpEntity(
                            new IdDto(taskId),
                            jwtResponseEntity.getBody().getAccessToken()),
                    Void.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class getAllTasks {
        @Test
        void authenticatedUser_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());
            when(taskService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(taskDto)));

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
            assertNotNull(taskResponseEntity.getBody());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            null),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class getTasksListByCreator {
        @Test
        void authenticatedUser_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());
            when(taskService.findAllByCreator(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(taskDto)));

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all/creator/{id}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class,
                    adminId);

            assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
            assertNotNull(taskResponseEntity.getBody());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all/creator/{id}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            null),
                    TaskDto.class,
                    adminId);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class getTasksListByExecutor {
        @Test
        void authenticatedUser_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());
            when(taskService.findAllByExecutor(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(taskDto)));

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all/executor/{id}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class,
                    userId);

            assertEquals(HttpStatus.OK, taskResponseEntity.getStatusCode());
            assertNotNull(taskResponseEntity.getBody());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.exchange(
                    baseUrl() + "/tasks/all/executor/{id}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            null),
                    TaskDto.class,
                    userId);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }
}