package com.test.api.service;

import com.test.api.exception.*;
import com.test.api.repository.UserRepository;
import com.test.api.user.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public User addUser(@Valid User user) {

        try{
            if(userRepository.existsByLoginAndPasswordIgnoreCase(user.getLogin(), user.getPassword())){
                throw new UserAlreadyExistsException("Such user (login:" + user.getLogin() + ") already exists");
            }

            userRepository.save(user);
            if(userRepository.existsByLoginAndPasswordIgnoreCase(user.getLogin(), user.getPassword())){
                return user;
            }
            throw new ServerDBException("Error with saving new user in the User Table");
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }


    }

    @Override
    public User updateUser(@Valid User user) {

        try{
            userRepository.findById(user.getId()).orElseThrow(
                    () -> new IdNotFoundException("User not found, id: " + user.getId()));

            userRepository.save(user);
            return user;

        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public Long deleteUser(Long id){

        try{

            if(userRepository.existsById(id)){
                userRepository.deleteById(id);
                return id;
            }
            else throw new IdNotFoundException("User not found, id " + id);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteListOfUsersById(Long startId, Long endId) {

        try{
            userRepository.deleteListOfUsersById(startId, endId);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public void deleteListOfUsersByStartAndEndId(Long startId, Long endId){
        try{
            userRepository.deleteListOfUsersByStartAndEndId(startId, endId);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteListOfUsersByStartIdAsc(Long startId){
        try{
            userRepository.deleteListOfUsersByStartIdAsc(startId);
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }
    }



    @Override
    public User getUserById(Long id) {

        try{
            return userRepository.findById(id).orElseThrow(() -> new IdNotFoundException("User not found, id: " + id));
        }
        catch (DataAccessException e) {
            throw new OurDataAccessException(e.getMessage());
        }

    }

    @Override
    public List<User> getAllUsers(){

        if(userRepository.count()>0){
            try{
                return userRepository.findAll();
            }
            catch (DataAccessException e) {
                throw new OurDataAccessException(e.getMessage());
            }
        }
        else throw new NoContentException("User table is empty");

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
