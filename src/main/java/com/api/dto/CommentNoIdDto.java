package com.api.dto;

import com.api.entity.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
