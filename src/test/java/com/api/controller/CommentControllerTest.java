package com.api.controller;

import com.api.dto.*;
import com.api.dto.jwt.JwtRequestDto;
import com.api.dto.jwt.JwtResponseDto;
import com.api.repository.TaskRepository;
import com.api.repository.UserRepository;
import com.api.service.auth.AuthServiceImpl;
import com.api.service.interfaces.CommentService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerTest {

    @LocalServerPort
    private int port;
    @MockBean
    private CommentService commentService;
    @MockBean
    private TaskRepository taskRepository; // to check if executor
    @Autowired
    private UserRepository userRepository; // real users from the DB to check role permission
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AuthServiceImpl authService;

    private UUID commentId;
    private UUID taskId;
    private UUID adminId;
    private UUID executorId;
    private UUID nonExecutorId;
    private CommentDto commentDto;
    private UserDto executorDto;
    private UserDto adminDto;
    private UserDto nonExecutorDto;

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

        executorId = UUID.fromString("a88589c6-0f3a-47fc-8a43-78f9f9bb78ff");
        executorDto = modelMapper.map(userRepository.findById(executorId), UserDto.class);

        nonExecutorId = UUID.fromString("892a0f4d-3615-43fd-b3d2-90171fac84df");
        nonExecutorDto = modelMapper.map(userRepository.findById(nonExecutorId), UserDto.class);

        taskId = UUID.randomUUID();
        commentId = UUID.randomUUID();
        commentDto = CommentDto.builder()
                .id(commentId)
                .text("Test comment text")
                .author(adminDto)
                .build();
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
        authService.getTokenStore().clear();
    }

    @Nested
    class addComment {

        record InvalidDtoCase(String name, CommentNoIdDto dto) {
            @Override
            public String toString() {
                return name;
        }}

        private static Stream<Arguments> invalidDtos() {
            return Stream.of(
                    Arguments.of(new InvalidDtoCase(
                            "text - null | author - null", new CommentNoIdDto(null, null))),
                    Arguments.of(new InvalidDtoCase(
                            "text - empty | author - null", new CommentNoIdDto("", null)))
            );
        }

        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(commentService.addComment(any(UUID.class), any(CommentNoIdDto.class))).thenReturn(commentDto);

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/comments/task/{taskId}",
                    getHttpEntity(
                            modelMapper.map(commentDto, CommentNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.CREATED, commentResponseEntity.getStatusCode());
            assertNotNull(commentResponseEntity.getBody());
            assertEquals(commentId, commentResponseEntity.getBody().getId());

        }
        @Test
        void executor_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(executorDto.getEmail(), executorDto.getPassword());
            when(taskRepository.existsByIdAndExecutorEmail(taskId, executorDto.getEmail())).thenReturn(true);
            when(commentService.addComment(any(UUID.class), any(CommentNoIdDto.class))).thenReturn(commentDto);

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/comments/task/{taskId}",
                    getHttpEntity(
                            modelMapper.map(commentDto, CommentNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.CREATED, commentResponseEntity.getStatusCode());
            assertNotNull(commentResponseEntity.getBody());
            assertEquals(commentId, commentResponseEntity.getBody().getId());

        }
        @Test
        void nonExecutorOrNonAdmin_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(nonExecutorDto.getEmail(), nonExecutorDto.getPassword());

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/comments/task/{taskId}",
                    getHttpEntity(
                            modelMapper.map(commentDto, CommentNoIdDto.class),
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.FORBIDDEN, commentResponseEntity.getStatusCode());

        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/comments/task/{taskId}",
                    getHttpEntity(
                            modelMapper.map(commentDto, CommentNoIdDto.class),
                            null),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.FORBIDDEN, commentResponseEntity.getStatusCode());

        }
        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidDtos")
        void invalidDto_shouldReturn400(InvalidDtoCase invalidDto){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(commentService.addComment(any(UUID.class), any(CommentNoIdDto.class))).thenReturn(commentDto);

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.postForEntity(
                    baseUrl() + "/comments/task/{taskId}",
                    getHttpEntity(
                            invalidDto.dto(),
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.BAD_REQUEST, commentResponseEntity.getStatusCode());
        }
    }

    @Nested
    class getTaskComments {
        @Test
        void admin_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(adminDto.getEmail(), adminDto.getPassword());
            when(commentService.findAllByTaskId(any(UUID.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(commentDto)));

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.exchange(
                    baseUrl() + "/comments/task/{taskId}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.OK, commentResponseEntity.getStatusCode());
            assertNotNull(commentResponseEntity.getBody());
        }
        @Test
        void executor_success(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(executorDto.getEmail(), executorDto.getPassword());
            when(taskRepository.existsByIdAndExecutorEmail(taskId, executorDto.getEmail())).thenReturn(true);
            when(commentService.findAllByTaskId(any(UUID.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(commentDto)));

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.exchange(
                    baseUrl() + "/comments/task/{taskId}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.OK, commentResponseEntity.getStatusCode());
            assertNotNull(commentResponseEntity.getBody());
        }
        @Test
        void nonExecutorOrNonAdmin_shouldReturn403(){
            ResponseEntity<JwtResponseDto> jwtResponseEntity = login(nonExecutorDto.getEmail(), nonExecutorDto.getPassword());
            when(commentService.findAllByTaskId(any(UUID.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(commentDto)));

            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.exchange(
                    baseUrl() + "/comments/task/{taskId}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            jwtResponseEntity.getBody().getAccessToken()),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.FORBIDDEN, commentResponseEntity.getStatusCode());
        }
        @Test
        void unauthenticatedUser_shouldReturn403(){
            ResponseEntity<CommentDto> commentResponseEntity = restTemplate.exchange(
                    baseUrl() + "/comments/task/{taskId}",
                    HttpMethod.GET,
                    getHttpEntity(
                            null,
                            null),
                    CommentDto.class,
                    taskId);

            assertEquals(HttpStatus.FORBIDDEN, commentResponseEntity.getStatusCode());
        }
    }
}