package com.api.service;

import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.entity.Task;
import com.api.repository.TaskRepository;
import com.api.service.interfaces.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Task task = taskRepository.save(modelMapper.map(taskDto, Task.class));
        return modelMapper.map(task, TaskDto.class);
    }

    @Override
    public void deleteTask(IdDto idDto) {
        taskRepository.deleteById(idDto.id());
    }




//    @Override
//    public Page<TaskDto> getAllTasks(Pageable pageable) {
//        Page<Task> tasks = taskRepository.findAll(pageable);
//        return tasks.map(task -> modelMapper.map(task, TaskDto.class));
//    }

    // SIMPLE LIST
//    @Override
//    public List<TaskDto> getAllTasks() {
//        List<Task> tasks = taskRepository.findAll();
//        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
//    }

    // PAGING
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
