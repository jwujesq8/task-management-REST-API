package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskDto {

        @NotNull(message = "Task must have an id")
        @Schema(description = "task id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        private UUID id;

        @NotNull(message = "Task must have a title")
        @Size(
                max = 250,
                min = 1,
                message = "Acceptable task size is 1-250"
        )
        @Schema(description = "tasks title",
                example = "Title 0")
        private String title;

        @Size(
                max = 256,
                message = "Acceptable task description is 0-256"
        )
        @Schema(
                description = "tasks description",
                example = "Task 0 description"
        )
        private String description;

        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Statuses that are acceptable: в ожидании|в процессе|завершено")
        @Schema(description = "Task status name: в ожидании|в процессе|завершено", example = "в ожидании")
        private String status;

        @NotNull(message = "Task must have a priority")
        @Pattern(regexp = "^(высокий|средний|низкий)$",
                message = "Priorities that are acceptable: высокий|средний|низкий")
        @Schema(description = "task priority: в ожидании|в процессе|завершено", example = "в ожидании")
        private String priority;

        @NotNull(message = "Creator must be")
        @Schema(description = "tasks user-creator")
        private UserDto creator;

        @NotNull(message = "Executor must be")
        @Schema(description = "tasks user-executor")
        private UserDto executor;

}
