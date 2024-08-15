package com.test.api.dto.request;

import com.test.api.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDto {
    private String login;
    private String password;
    private String fullName;
    private Gender gender;
}
