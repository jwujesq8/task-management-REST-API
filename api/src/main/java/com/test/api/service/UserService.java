package com.test.api.service;

import com.test.api.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    String addUser(User user);

    User updateUser(Long id, User user);

    Long deleteUser(Long id);

    boolean existsByLoginAndPasswordIgnoreCase(String login, String password);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    String deleteListOfUsersById(Long startId, Long endId);

    Optional<User> getUserByLogin(String login);
}
