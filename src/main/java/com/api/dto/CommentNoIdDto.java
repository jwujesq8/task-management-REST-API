package com.api.dto;

import com.api.entity.Task;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentNoIdDto{

    @NotNull(message = "Comment must have a text")
    private String text;

    @NotNull(message = "Comment must have an author")
    private UserDto author;
}
