package com.api.controller;

import com.api.config.Role;
import com.api.dto.TaskDto;
import com.api.dto.UserDto;
import com.api.dto.jwt.JwtRequestDto;
import com.api.dto.jwt.JwtResponseDto;
import com.api.dto.jwt.RefreshJwtRequestDto;
import com.api.repository.TaskRepository;
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
import org.springframework.core.task.TaskRejectedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private AuthServiceImpl authService;
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

    @BeforeEach
    void setUp(){
        adminId = UUID.randomUUID();
        adminDto = UserDto.builder()
                .id(adminId)
                .fullName("Test Admin")
                .email("admin@gmail.com")
                .password("admin-123")
                .role(Role.ADMIN)
                .build();
        userId = UUID.randomUUID();
        userDto = UserDto.builder()
                .id(userId)
                .fullName("Test User")
                .email("user@gmail.com")
                .password("user-123")
                .role(Role.USER)
                .build();
        taskId = UUID.randomUUID();
        taskDto = TaskDto.builder()
                .id(taskId)
                .title("Task title")
                .description("Task description")
                .priority("mid")
                .status("in progress")
                .creator(adminDto)
                .executor(userDto)
                .build();
    }

//    @AfterEach
//    void tearDown() {
//        reset(taskRepository);
//        SecurityContextHolder.clearContext();
//        authService.getRefreshTokensStorage().clear();
//    }

    HttpEntity<RefreshJwtRequestDto> getRequestEntity(JwtResponseDto jwtResponseDto){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtResponseDto.getAccessToken());
        return new HttpEntity<>(RefreshJwtRequestDto.builder()
                .refreshJwtRequest(jwtResponseDto.getRefreshToken())
                .build(), headers);
    }


    @Nested
    class addTask{
        @Test
        void success(){

        }
        @Test
        void addAlreadyExistingTask_shouldReturn400(){

        }
        @Test
        void notAdminTryToAddTask_shouldReturn403(){

        }
        @Test
        void nonAuthenticatedUserTryToAddTask_shouldReturn403(){

        }
    }

    @Nested
    class updateTask{
        @Test
        void success(){

        }
        // todo: parameterized
        @Test
        void nonValidDto_shouldReturn400(){

        }
        @Test
        void notAdminTryToUpdateTask_shouldReturn403(){

        }
        @Test
        void nonAuthenticatedUserTryToAddTask_shouldReturn403(){

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