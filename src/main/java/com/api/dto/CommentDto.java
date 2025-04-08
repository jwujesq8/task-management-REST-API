package com.api.dto;

import com.api.entity.Task;
import com.api.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Schema(description = "comment id - UUID",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @NotNull(message = "Comment must have a text")
    @Size(
            min = 1,
            max = 250,
            message = "Acceptable comments text size is 1-250"
    )
    @Schema(description = "comment text",
            example = "comment text")
    private String text;

    @NotNull(message = "Comment must have an author")
    @Schema(description = "comments author")
    private UserDto author;

}
