package com.api.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskNoIdDto {
        @NotNull(message = "Task must have a title")
        @Size(
                max = 100,
                min = 1,
                message = "Non valid title size"
        )
        private String title;

        @Size(
                max = 256,
                message = "Non valid description size"
        )
        private String description;

        @Pattern(regexp = "^(в ожидании|в процессе|завершено)$",
                message = "Next statuses are acceptable: в ожидании|в процессе|завершено")
        private String status;

        @Pattern(regexp = "^(высокий|средний|низкий)$",
                message = "Next priorities are acceptable: высокий|средний|низкий")
        private String priority;

        @NotNull(message = "Creator must be")
        private UserDto creator;

        @NotNull(message = "Executor must be")
        private UserDto executo;
}
