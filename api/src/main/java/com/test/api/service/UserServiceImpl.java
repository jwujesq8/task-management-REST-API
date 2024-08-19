package com.test.api.service;

import com.test.api.exception.*;
import com.test.api.repository.UserRepository;
import com.test.api.user.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        try {
            userRepository.save(user);
            if(userRepository.existsByLoginAndPasswordIgnoreCase(user.getLogin(), user.getPassword())){
                return user;
            }
            throw new ServerDBException("Error with saving new user in the User Table");
        }
        catch (ConstraintViolationException e){
            throw new ValidException(e);
        }
    }

    @Override
    public User updateUser(Long id, User user) throws ValidException, HttpClientErrorException {

        User userDB = userRepository.findById(id).orElseThrow(
                () -> new IdNotFoundException("User not found, id: " + id));

        // login
        if(Objects.nonNull(user.getLogin()) &&
                !"".equalsIgnoreCase(user.getLogin())) {
            try { userDB.setLogin(user.getLogin());}
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }

        }
        // password
        if(Objects.nonNull(user.getPassword()) &&
                !"".equalsIgnoreCase(user.getPassword())) {
            try{userDB.setPassword(user.getPassword());}
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }

        }
        // fullName
        if(Objects.nonNull(user.getFullName()) &&
                !"".equalsIgnoreCase(user.getFullName())) {
            try{userDB.setFullName(user.getFullName());}
            catch (ConstraintViolationException valEx){
                throw new ValidException(valEx);
            }

        }
        // gender
        if(Objects.nonNull(user.getGender()) &&
                !"".equalsIgnoreCase(user.getGender().getName())) {
            try{userDB.setGender(user.getGender());}
            catch (HttpClientErrorException httpClientErrEx){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Wrong gender, id: " + user.getGender().getId());
            }

        }

        userRepository.save(userDB);
        return userDB;


    }

    @Override
    public Long deleteUser(Long id) throws HttpClientErrorException{

        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return id;

        }
        else throw new IdNotFoundException("User not found, id " + id);
    }

    @Override
    public void deleteListOfUsersById(Long startId, Long endId) {

        if (startId>0 && endId>=startId){
            userRepository.deleteListOfUsersById(startId, endId);
        }
        else throw new BadClientRequestException("Bad id's range for deleting users, " +
                "start id: " + startId + ", end id: " + endId);

    }

    @Override
    public Optional<User> getUserById(Long id) {

        if (userRepository.existsById(id)){
            return userRepository.findById(id);
        }
        else throw new IdNotFoundException("User not found, id: " + id);

    }

    @Override
    public List<User> getAllUsers(){

        if(userRepository.count()>0){
            return userRepository.findAll();
        }
        else throw new NoContentException("Empty all users list");

    }


    @Override
    public boolean existsByLoginAndPasswordIgnoreCase(String login, String password){
        try{return userRepository.existsByLoginAndPasswordIgnoreCase(login, password);}
        catch (ServerErrorException serverErrEx){
            throw new ObjectNotFoundException("No user with login " + login);
        }
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        try {return userRepository.findByLogin(login);}
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with User Table", null);}
    }
}
