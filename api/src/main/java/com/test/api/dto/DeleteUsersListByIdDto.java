package com.test.api.dto;

import com.test.api.annotation.validateStartEndId.ValidStartEndId;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@ValidStartEndId
public class DeleteUsersListByIdDto {

    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Must be a number")
    @Min(value = 1L, message = "Min value is 1")
    private Long startId;

    @NotEmpty
    @Pattern(regexp = "\\d+", message = "Must be a number")
    @Min(value = 1L, message = "Min value is 1")
    private Long endId;
}
