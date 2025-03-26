package com.api.controller;


import com.api.config.JWT.JwtAuthentication;
import com.api.dto.IdDto;
import com.api.dto.StatusDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.entity.User;
import com.api.service.interfaces.TaskService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Validated
@Slf4j

@Tag(name="Task controller", description="Interaction with users tasks")
public class TaskController {

    private final TaskService taskService;




    @PostMapping("/new")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResponseEntity<TaskDto> addTask(@RequestBody @Valid @NotNull TaskNoIdDto taskNoIdDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.addTask(taskNoIdDto));
    }





    @PutMapping
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    // TODO: executor may access to change only name not a whole task
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid @NotNull TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }
    @PutMapping("/{taskId}/status")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    public ResponseEntity<TaskDto> updateTaskStatus(@RequestBody @NotNull @Valid StatusDto status,
            @PathVariable UUID taskId){
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status.name()));
    }



    @DeleteMapping
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public void deleteTask(@RequestBody @Valid @NotNull IdDto idDto) {
        taskService.deleteTask(idDto);
    }





    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAll(PageRequest.of(page, size)));
    }






    @GetMapping("/all/creator")
    @PreAuthorize("isAuthenticated()")
    // TODO: add Optional public String getFoos(@RequestParam Optional<String> id){
    public ResponseEntity<Page<TaskDto>> getTasksListByCreator(@RequestParam UUID id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByCreator(id, PageRequest.of(page, size)));
    }





    @GetMapping("/all/executor")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<TaskDto>> getTasksListByExecutor(@RequestParam UUID id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByExecutor(id, PageRequest.of(page, size)));
    }

}
