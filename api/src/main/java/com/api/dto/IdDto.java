package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.UUID;

public record IdDto(
        @NotNull(message = "Task must have an id")
        @Schema(description = "task id - UUID",
                example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id
) {}
