package com.test.api.controller;

import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.UserActionMessageDto;
import com.test.api.modelMapper.UserRequestDtoToUserMapper;
import com.test.api.modelMapper.UserToUserRequestDTOMapper;
import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final GenderService genderService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserToUserRequestDTOMapper userToUserRequestDTOMapper;


    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> addUser(@Valid @RequestBody UserRequestDto userRequestDto) throws ValidationException {
        try {
            User user = userToUserRequestDTOMapper
            userService.addUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is saved", HttpStatus.CREATED.getReasonPhrase()));
        }
        catch (ValidationException valEx){
            throw new ValidationException("Non valid user parameters");
        }
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException(serverErrEx.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user){

        try{

            userService.updateUser(id, user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is updated, id: " + user.getId(), HttpStatus.CREATED.getReasonPhrase()));
        }
        catch (HttpClientErrorException httpClErrEx){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found, id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteUserById(@PathVariable("id") Long id){

        try{
            if(userService.deleteUser(id)==null){
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found, id: " + id);
            }
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("User is removed, id: " + id, HttpStatus.OK.getReasonPhrase()));
        }

        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException(serverErrEx.getMessage(), null);
        }
    }

    @DeleteMapping("/{startId}/{endId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersById(@PathVariable("startId") Long startId,
                                                                    @PathVariable("endId") Long endId){
        try{
            userService.deleteListOfUsersById(startId,endId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("Deleted users: " + startId + "-" + endId,
                            HttpStatus.OK.getReasonPhrase()));
        }
        catch (ServerErrorException serverErrEx) {
            throw new ServerErrorException("Server error with removing users with id in range " + startId + "-" + endId
                    , null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> getUserById(@PathVariable("id") Long id){

        //log.info("(userController) is authenticated : " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        try{
            return userService.getUserById(id);
        }
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with User Table", null);
        }
        catch (HttpClientErrorException httpClErrEx){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found, id: " + id);
        }

    }


    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<User> getAllUsers(){

        try{
            // webSocket message
            UserActionMessageDto message = new UserActionMessageDto();
            message.setUser((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            message.setAction("use request GET user/all");
            log.info("message: " + message.getUser() + ", action: " + message.getAction());
            messagingTemplate.convertAndSend("/topic", "use request GET user/all");
            return userService.getAllUsers();
        }
        catch (ServerErrorException serverErrEx){
            throw new ServerErrorException("Server error with User Table", null);
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String checkGenderTableAndWelcome() throws AuthenticationException {
        genderService.checkGenderTable();
        return "Hello!" ;
    }



    }

