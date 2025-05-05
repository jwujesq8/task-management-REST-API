package com.api.controller;

import com.api.config.Role;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.dto.UserDto;
import com.api.dto.error.ErrorMessageResponseDto;
import com.api.dto.jwt.JwtRequestDto;
import com.api.dto.jwt.JwtResponseDto;
import com.api.dto.jwt.RefreshJwtRequestDto;
import com.api.repository.TaskRepository;
import com.api.repository.UserRepository;
import com.api.service.AuthServiceImpl;
import com.api.service.interfaces.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

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

    @MockBean
    private TaskService taskService;
    @MockBean
    private TaskRepository taskRepository;

    private UUID taskId;
    private UUID adminId;
    private UUID userId;
    private TaskDto taskDto;
    private UserDto userDto;
    private UserDto adminDto;

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
        adminId = UUID.fromString("ecf72b35-4151-4439-a5a1-408d2ce330c5");
        adminDto = modelMapper.map(userRepository.findById(adminId), UserDto.class);

        userId = UUID.fromString("a88589c6-0f3a-47fc-8a43-78f9f9bb78ff");
        userDto = modelMapper.map(userRepository.findById(userId), UserDto.class);

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
        reset(taskRepository);
        SecurityContextHolder.clearContext();
//        authService.getRefreshTokensStorage().clear();
    }

    @Nested
    class addTask{
        @Test
        void adminTryToAddTask_success(){
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
        void notAdminTryToAddTask_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(userDto.getEmail(), userDto.getPassword());
//            when(taskService.addTask(any(TaskNoIdDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    getHttpEntity(
                            modelMapper.map(taskDto, TaskNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
        @Test
        void nonAuthenticatedUserTryToAddTask_shouldReturn403(){
//            when(taskService.addTask(any(TaskNoIdDto.class))).thenReturn(taskDto);

            ResponseEntity<TaskDto> taskResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/tasks/new",
                    modelMapper.map(taskDto, TaskNoIdDto.class),
                    TaskDto.class);

            assertEquals(HttpStatus.FORBIDDEN, taskResponseEntity.getStatusCode());
        }
    }

    @Nested
    class updateTask{
        @Test
        void adminTryToUpdateTask_success(){
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
        // todo: parameterized
        @Test
        void nonValidDto_shouldReturn400(){

        }
        @Test
        void notAdminTryToUpdateTask_shouldReturn403(){
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
        void nonAuthenticatedUserTryToUpdateTask_shouldReturn403(){
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
        @Test
        void success(){

        }
        // todo: parameterized
        @Test
        void nonValidTaskStatusDto_shouldReturn400(){

        }
        // todo: parameterized
        @Test
        void notAdminOrNotExecutorTryToUpdateTaskStatus_shouldReturn403(){

        }
    }

    @Nested
    class deleteTask {
        @Test
        void success(){

        }
        @Test
        void notAdminTryToDelete_shouldReturn403(){

        }
    }

    @Nested
    class getAllTasks {
        @Test
        void success(){

        }
        @Test
        void nonAuthenticatedUserTryToListTasks_shouldReturn403(){

        }
    }

    @Nested
    class getTasksListByCreator {
        @Test
        void success(){

        }
        @Test
        void nonAuthenticatedUserTryToListTasksByCreator_shouldReturn403(){

        }
    }

    @Nested
    class getTasksListByExecutor {
        @Test
        void success(){

        }
        @Test
        void nonAuthenticatedUserTryToListTasksByExecutor_shouldReturn403(){

        }
    }
}