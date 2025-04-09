package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * Class TaskDto
 *
 * Data Transfer Object (DTO) representing a task.
 * This DTO is used to transfer task data between the client and the server.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {

        /**
         * The unique identifier of the task.
         * This field is mandatory and must be a valid UUID.
         */
        @NotNull(message = "Task must have an id")
        @Schema(description = "task id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        private UUID id;

        /**
         * The title of the task.
         * The title must be between 1 and 250 characters.
         */
        @NotNull(message = "Task must have a title")
        @Size(
                max = 250,
                min = 1,
                message = "Acceptable task size is 1-250"
        )
        @Schema(description = "tasks title",
                example = "Title 0")
        private String title;

        /**
         * The description of the task.
         * The description can be up to 256 characters long.
         */
        @Size(
                max = 256,
                message = "Acceptable task description is 0-256"
        )
        @Schema(
                description = "tasks description",
                example = "Task 0 description"
        )
        private String description;

        /**
         * The status of the task.
         * The status must be one of the following: "pending", "in progress", or "completed".
         */
        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(pending|in progress|completed)$",
                message = "Statuses that are acceptable: pending|in progress|completed")
        @Schema(description = "Task status name: pending|in progress|completed", example = "completed")
        private String status;

        /**
         * The priority of the task.
         * The priority must be one of the following: "high", "mid", or "low".
         */
        @NotNull(message = "Task must have a priority")
        @Pattern(regexp = "^(high|mid|low)$",
                message = "Priorities that are acceptable: high|mid|low")
        @Schema(description = "task priority: high|mid|low", example = "low")
        private String priority;

        /**
         * The creator of the task.
         * This field must not be null and must reference a valid user.
         */
        @NotNull(message = "Creator must be")
        @Schema(description = "tasks user-creator")
        private UserDto creator;

        /**
         * The executor of the task.
         * This field must not be null and must reference a valid user.
         */
        @NotNull(message = "Executor must be")
        @Schema(description = "tasks user-executor")
        private UserDto executor;

}
