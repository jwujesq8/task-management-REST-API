package com.api.controller;


import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.service.interfaces.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    public TaskDto addTask(TaskNoIdDto taskNoIdDto) {
        return taskService.addTask(taskNoIdDto);
    }





    @PutMapping
    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    public TaskDto updateTask(TaskDto taskDto) {
        return taskService.updateTask(taskDto);
    }





    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    public void deleteTask(IdDto idDto) {
        taskService.deleteTask(idDto);
    }





    @GetMapping("/all")
//    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    public List<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }






    @GetMapping("/all/creator")
    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    // TODO: add Optional public String getFoos(@RequestParam Optional<String> id){
    public List<TaskDto> getTasksListByCreator(@RequestParam UUID id) {
        return taskService.getTasksListByCreator(id);
    }





    @GetMapping("/all/executor")
    @PreAuthorize("isAuthenticated()")
    // TODO: add role
    public List<TaskDto> getTasksListByExecutor(@RequestParam UUID id) {
        return taskService.getTasksListByExecutor(id);
    }

}
