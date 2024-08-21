package com.test.api.dto.response;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDto {

    private String login;
    private String fullName;
    private String gender;

}
