package com.test.api.service;

import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.exception.BadRequestException;
import com.test.api.exception.OkException;
import com.test.api.modelMapper.UserModelMapper;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserModelMapper userModelMapper;
    private final GenderRepository genderRepository;

    @Override
    public UserResponseDto getUserById(Long id) {

        User user =  userRepository.findById(id).orElseThrow(
                () -> new BadRequestException.IdNotFoundException("User not found, id: " + id));

        return userModelMapper.map_User_to_UserResponseDto(user);
    }

    @Override
    public UserResponseDto addUser(POSTUserRequestDto postUserRequestDto) {

        if(userRepository.existsByLoginAndPasswordIgnoreCase(
                postUserRequestDto.getLogin(), postUserRequestDto.getPassword())){
            throw new BadRequestException.UserAlreadyExistsException(
                    "Such user (login:" + postUserRequestDto.getLogin() + ") already exists");
        }
        User user = userModelMapper.map_POSTUserRequestDto_to_User(postUserRequestDto);
        userRepository.save(user);
        return userModelMapper.map_User_to_UserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(PUTUserRequestDto putUserRequestDto) {

        userRepository.findById(putUserRequestDto.getId()).orElseThrow(
                () -> new BadRequestException.IdNotFoundException("User not found, id: " + putUserRequestDto.getId()));

        Optional<Gender> putUserRequestDtoGender = genderRepository.findByNameIgnoreCase(putUserRequestDto.getGenderName());
        User putUser = userModelMapper.map_PUTUserRequestDto_to_User(putUserRequestDto);
        putUser.setGender(putUserRequestDtoGender.orElse(genderRepository.findByName("none")));

        userRepository.save(putUser);
        return userModelMapper.map_User_to_UserResponseDto(putUser);

    }

    @Override
    public UserResponseDto deleteUser(Long id){

        User userToDelete = userRepository.findById(id).orElseThrow(
                () -> new BadRequestException.IdNotFoundException("User not found, id: " + id)
        );
        userRepository.deleteById(id);
        return userModelMapper.map_User_to_UserResponseDto(userToDelete);

    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        List<User> userList = userRepository.findAll();

        if (!userList.isEmpty()) {
            List<UserResponseDto> userResponseDtoList = new ArrayList<>();
            userList
                    .forEach(user ->
                            userResponseDtoList.add(userModelMapper.map_User_to_UserResponseDto(user))

                    );
            return userResponseDtoList;
        } else {
            throw new OkException.NoContentException("User table is empty");
        }
    }

    @Override
    @Transactional
    public List<UserResponseDto> deleteListOfUsersByStartAndEndId(Long startId, Long endId){

        List<User> userListToDelete = userRepository.userWhichIdInRange(startId, endId);
        List<UserResponseDto> userResponseDtoListToDelete = new ArrayList<>();
        userListToDelete.forEach(
                user -> {
                    userResponseDtoListToDelete.add(userModelMapper.map_User_to_UserResponseDto(user));

                }
        );

        if(!userResponseDtoListToDelete.isEmpty()){
            userRepository.deleteListOfUsersByStartAndEndId(startId, endId);
            return userResponseDtoListToDelete;
        }
        throw new BadRequestException.IdNotFoundException("There are no one user with id in range " + startId + "-" + endId);

    }

    @Override
    @Transactional
    public List<UserResponseDto> deleteListOfUsersByStartIdAsc(Long startId){

        List<User> userListToDelete = userRepository.userWhichIdFrom(startId);
        List<UserResponseDto> userResponseDtoListToDelete = new ArrayList<>();
        userListToDelete.forEach(
                user -> {
                    userResponseDtoListToDelete.add(userModelMapper.map_User_to_UserResponseDto(user));
                }
        );

        if (!userResponseDtoListToDelete.isEmpty()){
            userRepository.deleteListOfUsersByStartIdAsc(startId);
            return userResponseDtoListToDelete;
        }
        else throw new BadRequestException.IdNotFoundException("There are no one user with id from " + startId);

    }


    @Override
    public boolean existsByLoginAndPasswordIgnoreCase(String login, String password){
        return userRepository.existsByLoginAndPasswordIgnoreCase(login, password);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
