package com.test.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponseDto {

    @Schema(description = "Date and time")
    private String dateTime;

    @NotBlank(message = "Description is required")
    @Schema(description = "response body message (fail|success etc)",
            example = "Description about going well or wrong", minLength = 0)
    private String description;



}