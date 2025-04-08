package com.api.config;

import com.api.exception.BadRequestException;
import com.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskPermissionChecker {

    private final TaskRepository taskRepository;

    public boolean isTaskExecutor(UUID taskId, String email) {
        if(taskRepository.existsByIdAndExecutorEmail(taskId, email)) return true;
        else throw new BadRequestException("Only executor and admin have access");
    }
}
