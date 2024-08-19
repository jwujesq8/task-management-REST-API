package com.test.api.service;

import com.test.api.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);

    User updateUser(Long id, User user);

    Long deleteUser(Long id);

    void deleteListOfUsersById(Long startId, Long endId);

    boolean existsByLoginAndPasswordIgnoreCase(String login, String password);

    boolean existsById(Long id);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    Optional<User> getUserByLogin(String login);
}
