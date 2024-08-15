package com.test.api.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class dtoUser {

    private String login;
    private String fullName;
    private String gender;

}
