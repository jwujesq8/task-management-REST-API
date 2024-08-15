package com.test.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequestDto {

    private String login;
    private String password;
}
