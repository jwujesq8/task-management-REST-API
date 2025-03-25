package com.api.config;

import com.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskPermissionChecker {

    private final TaskRepository taskRepository;

    public boolean isTaskExecutor(UUID taskId, String fullName) {
        return taskRepository.existsByIdAndExecutorFullName(taskId, fullName);
    }
}
