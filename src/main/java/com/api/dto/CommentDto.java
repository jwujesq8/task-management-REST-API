package com.api.dto;

import com.api.entity.Task;
import com.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;


public record CommentDto(
         UUID id,
         String text,
         User author,
         Task task
) {}
