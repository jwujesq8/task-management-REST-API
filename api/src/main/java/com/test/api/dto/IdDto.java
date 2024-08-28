package com.test.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IdDto {

    @NotNull(message = "Id must not be null")
    @Min(value = 1L, message = "Min value is 1")
    @Max(value = Long.MAX_VALUE, message = "Crossed max value")
    @Schema(description = "user id", example = "1")
    private Long id;
}
