package com.api.service.validation;

import com.api.entity.Task;
import com.api.exception.BadRequestException;
import com.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskValidator {

    private final TaskRepository taskRepository;

    public Task findByIdOrThrowBadRequest(UUID id){
        return taskRepository.findById(id).orElseThrow(
                () -> new BadRequestException("There is no such task"));
    }

    public boolean isStatusEqualTo(Task task, String newStatus){
        return task.getStatus().equals(newStatus);
    }
}
