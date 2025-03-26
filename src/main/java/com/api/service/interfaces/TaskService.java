package com.api.service.interfaces;

import com.api.config.JWT.JwtAuthentication;
import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface TaskService {

    TaskDto addTask(TaskNoIdDto taskNoIdDto);
    TaskDto updateTask(TaskDto taskDto);
    TaskDto updateTaskStatus(UUID taskId, String newStatus);
    void deleteTask(IdDto idDto);


    Page<TaskDto> findAll(Pageable pageable);
    Page<TaskDto> findAllByCreator(UUID idCreator, Pageable pageable);
    Page<TaskDto> findAllByExecutor(UUID idExecutor, Pageable pageable);

}
