package com.test.api.entity;

import com.test.api.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActionMessage {
    private String user;
    private String action;

}

