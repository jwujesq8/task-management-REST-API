package com.api.service;

import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.entity.Comment;
import com.api.entity.Task;
import com.api.exception.BadRequestException;
import com.api.repository.TaskRepository;
import com.api.service.interfaces.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;


    @Override
    public TaskDto addTask(TaskNoIdDto taskNoIdDto) {
        Task task = taskRepository.save(modelMapper.map(taskNoIdDto, Task.class));
        return modelMapper.map(task, TaskDto.class);
    }


    @Transactional
    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task taskOld = taskRepository.findById(taskDto.getId()).orElseThrow(
                () -> new BadRequestException("Provided task doesn't exist"));
        List<Comment> comments = taskOld.getComments();
        Task task = modelMapper.map(taskDto, Task.class);
        task.setComments(comments);
        taskRepository.save(task);
        return modelMapper.map(task, TaskDto.class);
    }


    @Override
    public TaskDto updateTaskStatus(UUID taskId, String newStatus) {
        Task taskDB = taskRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException("Provided task doesn't exist"));
        if (taskDB.getStatus().equals(newStatus)) {
            throw new BadRequestException("The new status must be different from the current status");
        }
        taskDB.setStatus(newStatus);
        return modelMapper.map(taskRepository.save(taskDB), TaskDto.class);
    }


    @Override
    public void deleteTask(IdDto idDto) {
        taskRepository.deleteById(idDto.id());
    }


    @Override
    public Page<TaskDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(task -> modelMapper.map(task, TaskDto.class));
    }


    public Page<TaskDto> findAllByCreator(UUID idCreator, Pageable pageable) {
        return taskRepository.findAllByCreatorId(idCreator, pageable)
                .map(task -> modelMapper.map(task, TaskDto.class));
    }


    public Page<TaskDto> findAllByExecutor(UUID idExecutor, Pageable pageable) {
        return taskRepository.findAllByExecutorId(idExecutor, pageable)
                .map(task -> modelMapper.map(task, TaskDto.class));
    }

}