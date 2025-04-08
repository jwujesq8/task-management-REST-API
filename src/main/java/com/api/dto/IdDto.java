package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

/**
 * Class IdDto
 *
 * Data Transfer Object (DTO) representing the ID of a task.
 * This DTO is used to transfer the task ID when required for operations like delete or update.
 */
public record IdDto(

        /**
         * The unique identifier of the task.
         * The task id is a required field and should be provided as a UUID.
         */
        @NotNull(message = "Task must have an id")
        @Schema(description = "task id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id
) {}
