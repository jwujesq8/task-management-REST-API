package com.test.api.service;

import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {


    UserResponseDto getUserById(Long id);

    UserResponseDto addUser(POSTUserRequestDto postUserRequestDto);

    UserResponseDto updateUser(PUTUserRequestDto putUserRequestDto);

    UserResponseDto deleteUser(Long id);

    List<UserResponseDto> getAllUsers();

    Long deleteListOfUsersById(Long startId, Long endId);

    Long deleteListOfUsersByStartAndEndId(Long startId, Long endId);

    Long deleteListOfUsersByStartIdAsc(Long startId);



    boolean existsByLoginAndPasswordIgnoreCase(String login, String password);

    boolean existsById(Long id);

    Optional<User> getUserByLogin(String login);
}
