package com.api.dto;

import com.api.entity.Task;
import com.api.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Class CommentDto
 *
 * Data Transfer Object (DTO) representing a comment.
 * This DTO includes the comment's id, text, and author.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto{

    /**
     * The unique identifier of the comment.
     */
    @NotNull(message = "Comment must have an id")
    @Schema(description = "comment id - UUID",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    /**
     * The text content of the comment.
     * The text must be between 1 and 250 characters long.
     */
    @NotNull(message = "Comment must have a text")
    @Size(
            min = 1,
            max = 250,
            message = "Acceptable comments text size is 1-250"
    )
    @Schema(description = "comment text",
            example = "comment text")
    private String text;

    /**
     * The author of the comment. This represents a user who created the comment.
     * The author must be provided.
     */
    @NotNull(message = "Comment must have an author")
    @Schema(description = "comments author")
    private UserDto author;

}
