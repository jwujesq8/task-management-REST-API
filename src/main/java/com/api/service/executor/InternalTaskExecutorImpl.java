package com.api.service.executor;

import com.api.entity.Task;
import com.api.exception.BadRequestException;
import com.api.service.executor.interfaces.InternalTaskExecutor;
import com.api.service.validation.TaskValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalTaskExecutorImpl implements InternalTaskExecutor {

    private final TaskValidator taskValidator;
    private final ModelMapper modelMapper;

    @Override
    public Task updateTask(Task taskExisting, Task newTask) {
        newTask.setComments(taskExisting.getComments());
        return newTask;
    }

    @Override
    public Task updateTaskStatus(Task task, String newStatus) {
        if (taskValidator.isStatusEqualTo(task,newStatus)) {
            throw new BadRequestException("The new status must be different from the current status");
        }
        task.setStatus(newStatus);
        return task;
    }
}
