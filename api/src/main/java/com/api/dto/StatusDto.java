package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StatusDto(
        @NotNull(message = "Task status must have a name")
        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Statuses that are acceptable: в ожидании|в процессе|завершено")
        @Schema(description = "Task status name: в ожидании|в процессе|завершено", example = "в ожидании")
        String name
) {
}
