package com.api.controller;

import com.api.dto.*;
import com.api.dto.error.ValidationErrorMessageResponseDto;
import com.api.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Class TaskController
 *
 * Controller for managing tasks.
 * Provides endpoints for creating, updating, retrieving, and deleting tasks.
 * The tasks can be created and managed only by users with appropriate roles.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name="Task controller", description="Interaction with users tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Endpoint to create a new task.
     * Accessible only by users with the ADMIN role.
     *
     * @param taskNoIdDto The request body containing task details (excluding the ID).
     * @return {@link TaskDto} containing the newly created task.
     */
    @PostMapping("/new")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    @Operation(summary = "post a new task (only for admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New task is created", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request (non valid data)",  content = @Content(schema = @Schema(implementation = ValidationErrorMessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid @NotNull TaskNoIdDto taskNoIdDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.addTask(taskNoIdDto));
    }

    /**
     * Endpoint to update an existing task.
     * Accessible only by users with the ADMIN role.
     *
     * @param taskDto The request body containing the updated task details.
     * @return {@link TaskDto} containing the updated task.
     */
    @PutMapping
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    @Operation(summary = "update/change task (only for admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task is updated", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request (non valid data)",  content = @Content(schema = @Schema(implementation = ValidationErrorMessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid @NotNull TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }

    /**
     * Endpoint to update the status of an existing task.
     * Accessible by users with the ADMIN role or the task executor.
     *
     * @param status The new status of the task.
     * @param taskId The ID of the task whose status is to be updated.
     * @return {@link TaskDto} containing the task with updated status.
     */
    @PatchMapping("/{taskId}/status")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    @Operation(summary = "update/change task status (for admin and executor)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks status is updated", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request (non valid data)",  content = @Content(schema = @Schema(implementation = ValidationErrorMessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated)",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<TaskDto> updateTaskStatus(@RequestBody @NotNull @Valid StatusDto status,
                                                    @PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status.name()));
    }

    /**
     * Endpoint to delete a task.
     * Accessible only by users with the ADMIN role.
     *
     * @param idDto The request body containing the task ID to be deleted.
     */
    @DeleteMapping
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    @Operation(summary = "delete task by task id (only for admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task is deleted", content = @Content(mediaType = "none")),
            @ApiResponse(responseCode = "400", description = "Bad request (non valid data)",  content = @Content(schema = @Schema(implementation = ValidationErrorMessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public void deleteTask(@RequestBody @Valid @NotNull IdDto idDto) {
        taskService.deleteTask(idDto);
    }

    /**
     * Endpoint to retrieve all tasks.
     * Accessible by authenticated users.
     *
     * @param page The page number for pagination.
     * @param size The number of tasks per page.
     * @return A {@link Page<TaskDto>} containing all tasks.
     */
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "get all tasks (for admin and user)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get tasks", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<Page<TaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAll(PageRequest.of(page, size)));
    }

    /**
     * Endpoint to retrieve tasks created by a specific user (creator).
     * Accessible by authenticated users.
     *
     * @param id The ID of the user who created the tasks.
     * @param page The page number for pagination.
     * @param size The number of tasks per page.
     * @return A {@link Page<TaskDto>} containing tasks created by the specified user.
     */
    @GetMapping("/all/creator/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "get tasks by creator id (for admin and user)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get tasks by creator id", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<Page<TaskDto>> getTasksListByCreator(@PathVariable UUID id,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByCreator(id, PageRequest.of(page, size)));
    }

    /**
     * Endpoint to retrieve tasks assigned to a specific user (executor).
     * Accessible by authenticated users.
     *
     * @param id The ID of the user who is the executor of the tasks.
     * @param page The page number for pagination.
     * @param size The number of tasks per page.
     * @return A {@link Page<TaskDto>} containing tasks assigned to the specified user.
     */
    @GetMapping("/all/executor/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "get tasks by executor id (for admin and user)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get tasks by creator executor", content = @Content(schema = @Schema(implementation = TaskDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated) or access denied",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<Page<TaskDto>> getTasksListByExecutor(@PathVariable UUID id,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByExecutor(id, PageRequest.of(page, size)));
    }

}
