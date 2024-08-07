package com.test.api.service;

import com.test.api.repository.UserRepository;
import com.test.api.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public String addUser(User user) {
        userRepository.save(user);

        if(userRepository.existsByLoginAndPasswordIgnoreCase(user.getLogin(),user.getPassword())){
            return user.getLogin();
        }
        return null;
    }

    @Override
    public User updateUser(Long id, User user) {

        User userDB = userRepository.findById(id).get();

        // login
        if(Objects.nonNull(user.getLogin()) &&
                !"".equalsIgnoreCase(user.getLogin())) {
            userDB.setLogin(user.getLogin());

        }
        // password
        if(Objects.nonNull(user.getPassword()) &&
                !"".equalsIgnoreCase(user.getPassword())) {
            userDB.setPassword(user.getPassword());

        }
        // fullName
        if(Objects.nonNull(user.getFullName()) &&
                !"".equalsIgnoreCase(user.getFullName())) {
            userDB.setFullName(user.getFullName());

        }
        // gender
        if(Objects.nonNull(user.getGender()) &&
                !"".equalsIgnoreCase(user.getGender().getName())) {
            userDB.setGender(user.getGender());

        }

        return userRepository.save(userDB);
    }

    @Override
    public Long deleteUser(Long id) {

        userRepository.deleteById(id);

        if(!userRepository.existsById(id)){
            return id;
        }

        return null;
    }

    @Override
    public boolean existsByLoginAndPasswordIgnoreCase(String login, String password){
        return userRepository.existsByLoginAndPasswordIgnoreCase(login, password);
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
    public String deleteListOfUsersById(Long startId, Long endId) {

        StringBuilder undeletedUsers = new StringBuilder();
        userRepository.deleteListOfUsersById(startId, endId);

        for(Long id=startId;id<=endId;id++){
            if(userRepository.existsById(id)) {
                undeletedUsers.append("  ").append(id);}

        }
        if (undeletedUsers.isEmpty()) {
            return  "Deleted users: " + startId + "-" + endId;
        }
        else{
            return  "Id list of undeleted users:" + undeletedUsers;
        }
    }
}
