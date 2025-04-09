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
         * The status must be one of the predefined values: "pending", "in progress", or "completed".
         */
        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(pending|in progress|completed)$",
                message = "Statuses that are acceptable: pending|in progress|completed")
        @Schema(description = "Task status name: pending|in progress|completed", example = "completed")
        String name
) {
}
