package com.test.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DeleteUsersListByIdDto {

    @NotEmpty
    private Long startId;
    @NotEmpty
    private Long endId;
}
