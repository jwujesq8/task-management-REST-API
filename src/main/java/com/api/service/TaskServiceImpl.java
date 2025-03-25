package com.api.service;

import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import com.api.entity.Task;
import com.api.repository.TaskRepository;
import com.api.service.interfaces.TaskService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
//    private final TaskModelMapper taskModelMapper;
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
    @Override
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
    }







    @Override
    public List<TaskDto> getTasksListByCreator(UUID idCreator) {
        List<Task> tasks = taskRepository.findAllByCreatorId(idCreator);
        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
    }

    @Override
    public List<TaskDto> getTasksListByExecutor(UUID idExecutor) {
        List<Task> tasks = taskRepository.findAllByExecutorId(idExecutor);
        return tasks.stream().map(task -> modelMapper.map(task, TaskDto.class)).toList();
    }
}
