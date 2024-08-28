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

    @Schema(description = "Date and time", example = "08.08.08 15:18:13")
    private String dateTime;

    @NotBlank(message = "Description must not be blank")
    @Schema(description = "response body message (fail|success etc)",
            example = "Summary of successes and issues", minLength = 0)
    private String description;



}