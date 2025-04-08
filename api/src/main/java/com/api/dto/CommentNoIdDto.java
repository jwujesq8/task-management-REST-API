package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class CommentNoIdDto
 *
 * Data Transfer Object (DTO) representing a comment without an id.
 * This DTO is used for creating new comments or when the id is not required.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentNoIdDto{

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
