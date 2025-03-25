package com.api.dto;

import com.api.entity.Task;
import com.api.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto{

    @NotNull(message = "Comment must have an id")
    private UUID id;

    @NotNull(message = "Comment must have a text")
    private String text;

    @NotNull(message = "Comment must have an author")
    private UserDto author;

}
