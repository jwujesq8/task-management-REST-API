package com.api.service.executor.interfaces;

import com.api.entity.Task;

public interface InternalTaskExecutor {

    Task updateTask(Task taskExisting, Task newTask);
    Task updateTaskStatus(Task task, String newStatus);
}
