package com.api.controller;


import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskDto.getId(), authentication.principal))")
    // TODO: executor may access to change only status not a whole task
    public ResponseEntity<TaskDto> updateTask(@RequestBody @Valid @NotNull TaskDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto));
    }





    @DeleteMapping
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public void deleteTask(@RequestBody @Valid @NotNull IdDto idDto) {
        taskService.deleteTask(idDto);
    }





    @GetMapping("/all")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResponseEntity<Page<TaskDto>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAll(PageRequest.of(page, size)));
    }






    @GetMapping("/all/creator")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    // TODO: add Optional public String getFoos(@RequestParam Optional<String> id){
    public ResponseEntity<Page<TaskDto>> getTasksListByCreator(@RequestParam UUID id,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByCreator(id, PageRequest.of(page, size)));
    }





    @GetMapping("/all/executor")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResponseEntity<Page<TaskDto>> getTasksListByExecutor(@RequestParam UUID id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(taskService.findAllByExecutor(id, PageRequest.of(page, size)));
    }

}
