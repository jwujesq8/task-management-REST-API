package com.api.service.interfaces;

import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/**
 * Class TaskService
 *
 * Service interface for handling operations related to tasks.
 * This interface defines methods for creating, updating, deleting, and retrieving tasks.
 */
public interface TaskService {

    /**
     * Adds a new task.
     *
     * @param taskNoIdDto The DTO containing the task details excluding the ID.
     * @return A DTO representing the added task, including its ID.
     */
    TaskDto addTask(TaskNoIdDto taskNoIdDto);

    /**
     * Updates an existing task.
     *
     * @param taskDto The DTO containing the updated task details, including its ID.
     * @return A DTO representing the updated task.
     */
    TaskDto updateTask(TaskDto taskDto);

    /**
     * Updates the status of a specific task.
     *
     * @param taskId The ID of the task whose status will be updated.
     * @param newStatus The new status to set for the task.
     * @return A DTO representing the updated task.
     */
    TaskDto updateTaskStatus(UUID taskId, String newStatus);

    /**
     * Deletes a task by its ID.
     *
     * @param idDto The DTO containing the task ID.
     */
    void deleteTask(IdDto idDto);

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param pageable The pagination information, including page number and size.
     * @return A page of tasks.
     */
    Page<TaskDto> findAll(Pageable pageable);

    /**
     * Retrieves all tasks assigned to a specific creator, with pagination support.
     *
     * @param idCreator The ID of the creator whose tasks will be retrieved.
     * @param pageable The pagination information, including page number and size.
     * @return A page of tasks created by the specified user.
     */
    Page<TaskDto> findAllByCreator(UUID idCreator, Pageable pageable);

    /**
     * Retrieves all tasks assigned to a specific executor, with pagination support.
     *
     * @param idExecutor The ID of the executor whose tasks will be retrieved.
     * @param pageable The pagination information, including page number and size.
     * @return A page of tasks assigned to the specified user.
     */
    Page<TaskDto> findAllByExecutor(UUID idExecutor, Pageable pageable);

}
