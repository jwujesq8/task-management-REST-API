package com.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Class TaskNoIdDto
 *
 * Data Transfer Object (DTO) representing a task without an ID.
 * This DTO is used to transfer task creation or update data between the client and the server.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskNoIdDto {

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
         * The status must be one of the following: "в ожидании", "в процессе", or "завершено".
         */
        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Statuses that are acceptable: в ожидании|в процессе|завершено")
        @Schema(description = "Task status name: в ожидании|в процессе|завершено", example = "в ожидании")
        private String status;

        /**
         * The priority of the task.
         * The priority must be one of the following: "высокий", "средний", or "низкий".
         */
        @NotNull(message = "Task must have a priority")
        @Pattern(regexp = "^(высокий|средний|низкий)$",
                message = "Priorities that are acceptable: высокий|средний|низкий")
        @Schema(description = "task priority: в ожидании|в процессе|завершено", example = "в ожидании")
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
