package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Class StatusDto
 *
 * Data Transfer Object (DTO) representing the status of a task.
 * This DTO is used to transfer and validate the task status, ensuring it conforms to specific values.
 */
public record StatusDto(

        /**
         * The name of the task status.
         * The status must be one of the predefined values: "в ожидании", "в процессе", or "завершено".
         */
        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Statuses that are acceptable: в ожидании|в процессе|завершено")
        @Schema(description = "Task status name: в ожидании|в процессе|завершено", example = "в ожидании")
        String name
) {
}
