package com.test.api.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserTokens {

    private String accessToken;
    private String refreshToken;

}
