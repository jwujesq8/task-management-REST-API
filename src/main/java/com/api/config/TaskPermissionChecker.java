package com.api.config;

import com.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskPermissionChecker {

    private final TaskRepository taskRepository;

    public boolean isTaskExecutor(UUID taskId, String email) {
        Boolean access = taskRepository.existsByIdAndExecutorEmail(taskId, email);
        log.info("access for " + email + " - " + access);
        return access;
    }
}
