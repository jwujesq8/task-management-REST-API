package com.test.api.controller;

import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GenderService genderService;



    @GetMapping()
    public void checkGenderTable(){
        genderService.checkGenderTable();
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){

        // TODO add webSocket message
        return userService.getAllUsers();
    }

//    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such User")  // 404
//    public class UserNotFoundException extends RuntimeException {
//        //
//    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id){

        return userService.getUserById(id);

//        if(response.isEmpty()) throw new UserNotFoundException();
//        else return response;
    }

    @PostMapping("")
    public String addUser(@Valid @RequestBody User user){

        // TODO add user validation
        return userService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public Long deleteUserById(@PathVariable("id") Long id){

        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user){

        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{startId}/{endId}")
    public String deleteListOfUsersById(@PathVariable("startId") Long startId, @PathVariable("endId") Long endId){
        return userService.deleteListOfUsersById(startId,endId);
    }
}

