package com.test.api.dto;

import com.test.api.annotation.validateStartEndId.ValidStartEndId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@Getter
@Setter
@ValidStartEndId(message = "End id must be greater or equal than start id (endId >= startId)")
public class DeleteUsersListByIdDto {

    @NotNull(message = "Start id must not be null")
    @Min(value = 1L, message = "Min value is 1")
    @Max(value = Long.MAX_VALUE, message = "Crossed max value")
    @Schema(description = "start id", example = "1")
    private Long startId;

    @NotNull(message = "End id must not be null")
    @Min(value = 1L, message = "Min value is 1")
    @Max(value = Long.MAX_VALUE, message = "Crossed max value")
    @Schema(description = "end id (must be greater or equal to start id)", example = "1")
    private Long endId;
}
