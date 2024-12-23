package com.test.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserActionMessageDto implements Serializable {

    @NotNull(message = "User must not be null")
    @Schema(description = "user login that send a request", minLength = 11, maxLength = 50)
    private String user;

    @NotEmpty(message = "Action must not be empty or null")
    @Schema(description = "action, specify a request type", minLength = 1)
    private String action;

}

