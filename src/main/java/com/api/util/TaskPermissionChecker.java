package com.api.util;

import com.api.exception.ForbiddenException;
import com.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Class TaskPermissionChecker
 *
 * Component that checks the permissions for a specific task.
 * Used to validate if a user has the necessary permissions to perform an action on a task.
 */
@Component
@RequiredArgsConstructor
public class TaskPermissionChecker {

    private final TaskRepository taskRepository;

    /**
     * Checks if the given user (identified by email) is the executor of the specified task.
     * If the user is not the executor, or if the task does not exist, an exception is thrown.
     *
     * @param taskId The unique identifier of the task.
     * @param email The email of the user attempting to access the task.
     * @return true if the user is the executor of the task; false otherwise.
     * @throws ForbiddenException If the user is not the executor or an admin.
     */
    public boolean isTaskExecutor(UUID taskId, String email) {
        if(taskRepository.existsByIdAndExecutorEmail(taskId, email)) return true;
        else throw new ForbiddenException("Only executor and admin have access");
    }
}
