package com.api.service.interfaces;

import com.api.dto.IdDto;
import com.api.dto.TaskDto;
import com.api.dto.TaskNoIdDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface TaskService {

    TaskDto addTask(TaskNoIdDto taskNoIdDto);
    TaskDto updateTask(TaskDto taskDto);
    void deleteTask(IdDto idDto);


    List<TaskDto> getAllTasks();


    List<TaskDto> getTasksListByCreator(UUID idCreator);
    List<TaskDto> getTasksListByExecutor(UUID idExecutor);

}
