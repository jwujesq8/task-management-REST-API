package com.api.service;

import com.api.config.enums.Role;
import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.dto.UserDto;
import com.api.entity.Task;
import com.api.entity.User;
import com.api.exception.BadRequestException;
import com.api.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ModelMapper modelMapper;

    private TaskServiceImpl taskService;

    private UUID taskId;
    private UUID adminId;
    private UUID userId;
    private UserDto adminDto;
    private User admin;
    private TaskDto taskDto;
    private TaskNoIdDto taskNoIdDto;
    private Task task;
    private Task updatedTask;
    private TaskDto updatedTaskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, modelMapper);

        taskId = UUID.randomUUID();
        adminId = UUID.randomUUID();
        adminDto = new UserDto(adminId,"Name Surname", "admin@gmail.com", "password456", Role.ADMIN);
        admin = new User(adminId, "Name Surname", "admin@gmail.com", "password456", Role.ADMIN);
        taskDto = new TaskDto(taskId, "title", "desc", "pending", "mid", adminDto, adminDto);
        taskNoIdDto = new TaskNoIdDto(taskDto.getTitle(), taskDto.getDescription(),
                taskDto.getStatus(), taskDto.getPriority(), taskDto.getCreator(), taskDto.getExecutor());
        task = Task.builder()
                .id(taskId)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .executor(admin)
                .creator(admin)
                .build();
        updatedTask = Task.builder()
                .id(taskId)
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status("completed")
                .priority(taskDto.getPriority())
                .executor(admin)
                .creator(admin)
                .build();
        updatedTaskDto = new TaskDto(taskId, "title", "desc", "completed", "mid", adminDto, adminDto);


    }

    @Test
    void addTask_ShouldReturnTaskDto() {

        when(modelMapper.map(taskNoIdDto, Task.class)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto result = taskService.addTask(taskNoIdDto);

        assertEquals(taskDto, result);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTaskDto() {

        when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.ofNullable(task));
        when(modelMapper.map(taskDto, Task.class)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        TaskDto result = taskService.updateTask(taskDto);

        assertEquals(taskDto, result);
        verify(taskRepository).save(task);
    }

    @Test
    void updateTaskStatus_ShouldThrowException_WhenTaskNotFound() {

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> taskService.updateTaskStatus(taskId, "DONE"));
    }

    @Test
    void updateTaskStatus_ShouldThrowException_WhenStatusIsSame() {

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        assertThrows(BadRequestException.class, () -> taskService.updateTaskStatus(taskId, task.getStatus()));
    }

    @Test
    void updateTaskStatus_ShouldUpdateStatus() {

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(updatedTask);
        when(modelMapper.map(updatedTask, TaskDto.class)).thenReturn(updatedTaskDto);

        TaskDto result = taskService.updateTaskStatus(task.getId(), "completed");

        assertEquals("completed", result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldInvokeRepository() {
        IdDto idDto = new IdDto(taskId);

        taskService.deleteTask(idDto);

        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void findAll_ShouldReturnPageOfTaskDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAll(pageable);
    }

    @Test
    void findAllByCreator_ShouldReturnPageOfTaskDto() {
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAllByCreatorId(admin.getId(), pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.findAllByCreator(admin.getId(), pageable);

        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAllByCreatorId(admin.getId(), pageable);
    }

    @Test
    void findAllByExecutor_ShouldReturnPageOfTaskDto() {
        Pageable pageable = PageRequest.of(0, 10);

        when(taskRepository.findAllByExecutorId(admin.getId(), pageable)).thenReturn(new PageImpl<>(List.of(task)));
        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDto);

        Page<TaskDto> result = taskService.findAllByExecutor(admin.getId(), pageable);

        assertEquals(1, result.getTotalElements());
        verify(taskRepository).findAllByExecutorId(admin.getId(), pageable);
    }
}