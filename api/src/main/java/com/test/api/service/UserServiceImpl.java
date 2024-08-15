package com.test.api.service;

import com.test.api.repository.UserRepository;
import com.test.api.user.User;
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
        userRepository.save(user);
        if(userRepository.existsByLoginAndPasswordIgnoreCase(user.getLogin(), user.getPassword())){
            return user;
        }
        return null;
    }

    @Override
    public User updateUser(Long id, User user) throws ValidationException, HttpClientErrorException {

        User userDB = userRepository.findById(id).get();

        // login
        if(Objects.nonNull(user.getLogin()) &&
                !"".equalsIgnoreCase(user.getLogin())) {
            try { userDB.setLogin(user.getLogin());}
            catch (ValidationException valEx){
                throw new ValidationException("Login requirements:" +
                        "\tlength 11-50" +
                        "\tunique among all users login");
            }

        }
        // password
        if(Objects.nonNull(user.getPassword()) &&
                !"".equalsIgnoreCase(user.getPassword())) {
            try{userDB.setPassword(user.getPassword());}
            catch (ValidationException valEx){
                throw new ValidationException("""
                        Password requirements:
                        \tlength 7-20
                        \t3 digit or more
                        \t1 special character or more
                        \tno white spaces\s""");
            }

        }
        // fullName
        if(Objects.nonNull(user.getFullName()) &&
                !"".equalsIgnoreCase(user.getFullName())) {
            try{userDB.setFullName(user.getFullName());}
            catch (ValidationException valEx){
                throw new ValidationException("""
                        Full name requirements:
                        \tlength 1-256
                        \tnot blank\s""");
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
        return null;
    }

    @Override
    public void deleteListOfUsersById(Long startId, Long endId) {
        userRepository.deleteListOfUsersById(startId, endId);
//        if (userRepository.existsByLog)
    }

    @Override
    public boolean existsByLoginAndPasswordIgnoreCase(String login, String password){
        try{return userRepository.existsByLoginAndPasswordIgnoreCase(login, password);}
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with function existsByLoginAndPasswordIgnoreCase()", null);
        }
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        try {return userRepository.findByLogin(login);}
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with User Table", null);}
    }
}
