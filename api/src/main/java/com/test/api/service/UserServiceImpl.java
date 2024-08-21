package com.test.api.service;

import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.exception.*;
import com.test.api.modelMapper.UserModelMapper;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        try{
            User user =  userRepository.findById(id).orElseThrow(
                    () -> new IdNotFoundException("User not found, id: " + id));

            return userModelMapper.convert_User_to_UserResponseDto(user);

        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public void addUser(POSTUserRequestDto postUserRequestDto) {

        try{
            if(userRepository.existsByLoginAndPasswordIgnoreCase(
                    postUserRequestDto.getLogin(), postUserRequestDto.getPassword())){
                throw new UserAlreadyExistsException(
                        "Such user (login:" + postUserRequestDto.getLogin() + ") already exists");
            }

            Gender postUserGender;

            if(postUserRequestDto.getGenderName().equalsIgnoreCase("male")){
                postUserGender = genderRepository.findByNameIgnoreCase("male");
            }
            else if (postUserRequestDto.getGenderName().equalsIgnoreCase("female")){
                postUserGender = genderRepository.findByNameIgnoreCase("female");
            }
            else {
                postUserGender = genderRepository.findByNameIgnoreCase("none");
            }

            postUserRequestDto.setGenderName(null);

            //TODO: String null genderName to Gender null
            User postUser = userModelMapper.convert_POSTUserRequestDto_to_User(postUserRequestDto);
            postUser.setGender(postUserGender);

            userRepository.save(postUser);
            if(userRepository.existsByLoginAndPasswordIgnoreCase(
                    postUser.getLogin(), postUser.getPassword())){

                return;
            }
            throw new ServerDBException("Error with saving new user in the User Table");
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }


    }

    @Override
    public void updateUser(PUTUserRequestDto putUserRequestDto) {

        try{
            userRepository.findById(putUserRequestDto.getId()).orElseThrow(
                    () -> new IdNotFoundException("User not found, id: " + putUserRequestDto.getId()));

            Gender putUserGender = genderRepository.findByNameIgnoreCase(putUserRequestDto.getGenderName());
            putUserRequestDto.setGenderName(null);
            User putUser = userModelMapper.convert_PUTUserRequestDto_to_User(putUserRequestDto);
            putUser.setGender(putUserGender);

            try{
                userRepository.save(putUser);
            }
            catch(ConstraintViolationException valEx){
                throw new ServerDBException("Validation error on a database level");
            }

            if(userRepository.existsByLoginAndPasswordIgnoreCase(
                    putUser.getLogin(), putUser.getPassword())){

                return;
            }
            throw new ServerDBException("Error with saving new user in the User Table");

        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteUser(Long id){

        try{
            if(userRepository.existsById(id)){

                userRepository.deleteById(id);

            }
            else throw new IdNotFoundException("User not found, id " + id);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        try {
            List<User> userList = userRepository.findAll();

            if (!userList.isEmpty()) {
                List<UserResponseDto> userResponseDtoList = new ArrayList<>();
                userList
                        .forEach(user ->
                                    userResponseDtoList.add(userModelMapper.convert_User_to_UserResponseDto(user))

                        );
                return userResponseDtoList;
            } else {
                throw new NoContentException("User table is empty");
            }
        } catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }
    }

    @Override
    public Long deleteListOfUsersById(Long startId, Long endId) {

        try{
            Long idCountInRange = userRepository.idCountInRange(startId, endId);
            if (idCountInRange>0){
                userRepository.deleteListOfUsersById(startId, endId);
                return idCountInRange;
            }
            else throw new IdNotFoundException("There are no one user with id in range " + startId + "-" + endId);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public Long deleteListOfUsersByStartAndEndId(Long startId, Long endId){

        try{
            Long idCountInRange = userRepository.idCountInRange(startId, endId);
            if (idCountInRange>0){
                userRepository.deleteListOfUsersByStartAndEndId(startId, endId);
                return idCountInRange;
            }
            else throw new IdNotFoundException("There are no one user with id in range " + startId + "-" + endId);

        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }
    }

    @Override
    public Long deleteListOfUsersByStartIdAsc(Long startId){

        try{
            Long idCountFrom = userRepository.idCountFrom(startId);
            if (idCountFrom>0){
                userRepository.deleteListOfUsersByStartIdAsc(startId);
                return idCountFrom;
            }
            else throw new IdNotFoundException("There are no one user with id from " + startId);

        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }
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
        try{
            return userRepository.findByLogin(login);
        }
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with User Table", null);}
    }
}
