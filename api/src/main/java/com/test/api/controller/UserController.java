package com.test.api.controller;

import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GenderService genderService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String checkGenderTableAndWelcome(){

        genderService.checkGenderTable();
        return "Hello! " ;
    }


    @GetMapping("/all")
    //@PreAuthorize("isAuthenticated()")
    public List<User> getAllUsers(){

        // TODO add webSocket message
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> getUserById(@PathVariable("id") Long id){

        return userService.getUserById(id);

//        if(response.isEmpty()) throw new UserNotFoundException();
//        else return response;
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public String addUser(@Valid @RequestBody User user){

        // TODO add user validation
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Long deleteUserById(@PathVariable("id") Long id){

        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public User updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user){

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{startId}/{endId}")
    @PreAuthorize("isAuthenticated()")
    public String deleteListOfUsersById(@PathVariable("startId") Long startId, @PathVariable("endId") Long endId){
        return userService.deleteListOfUsersById(startId,endId);
    }
}

