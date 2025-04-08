package com.api.service.interfaces;

import com.api.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByEmail(String login);
}
