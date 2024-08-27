package com.test.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActionMessageDto {

    @NotNull(message = "User is required")
    @Schema(description = "user login that send a request", minLength = 11, maxLength = 50)
    private String user;

    @NotEmpty(message = "Action is required")
    @Schema(description = "action, specify a request type", minLength = 1)
    private String action;

}

