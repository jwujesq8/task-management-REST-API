package com.test.api.controller;

import com.test.api.entity.UserActionMessage;
import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private GenderService genderService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String checkGenderTableAndWelcome(){

        genderService.checkGenderTable();
        return "Hello! " ;
    }


    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
//    @MessageMapping("/getAllUsers")
    public List<User> getAllUsers(){

        // TODO add webSocket message
        UserActionMessage message = new UserActionMessage();
//        message.setUser(SecurityContextHolder.getContext().getAuthentication() +
//                " (" +
//                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
//                + ")"
//        );
        message.setUser((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        message.setAction("use request GET user/all");
        log.info("message: " + message.getUser() + ", action: " + message.getAction());
        messagingTemplate.convertAndSend("/topic", message);

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

