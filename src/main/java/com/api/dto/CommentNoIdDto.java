package com.api.dto;

import com.api.entity.Task;


public record CommentNoIdDto(
        String text,
        UserDto author,
        Task task
) {}
