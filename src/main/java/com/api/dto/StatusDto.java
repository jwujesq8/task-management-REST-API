package com.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record StatusDto(
        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Next statuses are acceptable: в ожидании|в процессе|завершено")
        @Schema(description = "task status name: в ожидании|в процессе|завершено", example = "в ожидании")
        String name
) {
}
