package com.test.api.service;

import com.test.api.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User addUser(User user);

    User updateUser(User user);

    Long deleteUser(Long id);

    void deleteListOfUsersById(Long startId, Long endId);

    void deleteListOfUsersByStartAndEndId(Long startId, Long endId);

    void deleteListOfUsersByStartIdAsc(Long startId);

    boolean existsByLoginAndPasswordIgnoreCase(String login, String password);

    boolean existsById(Long id);

    User getUserById(Long id);

    List<User> getAllUsers();

    Optional<User> getUserByLogin(String login);
}
