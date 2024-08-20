package com.test.api.controller;

import com.test.api.dto.DeleteUsersListByIdDto;
import com.test.api.dto.IdDto;
import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.UserActionMessageDto;
import com.test.api.exception.*;
import com.test.api.modelMapper.UserModelMapper;
import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.User;
import jakarta.persistence.Id;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final GenderService genderService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserModelMapper userModelMapper;


    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> addUser(@RequestBody @Valid UserRequestDto userRequestDto){
        try {
            User user = userModelMapper.convert_UserRequestDto_to_User(userRequestDto);
            userService.addUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is saved!"));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @PutMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto){

        try{

            User user = userModelMapper.convert_UserRequestDto_to_User(userRequestDto);
            userService.updateUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is updated, id: " + user.getId()));

        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteUserById(@Valid IdDto idDto){

        try {
            userService.deleteUser(idDto.getId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("User is removed, id: " + idDto.getId()));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @DeleteMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersById(@Valid DeleteUsersListByIdDto deleteUsersListByIdDto){
        try{
            userService.deleteListOfUsersById(deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("Deleted users: " + deleteUsersListByIdDto.getStartId()
                            + "-" + deleteUsersListByIdDto.getEndId()));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @DeleteMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartAndEndId(@Valid DeleteUsersListByIdDto deleteUsersListByIdDto){
        try{
            userService.deleteListOfUsersByStartAndEndId(deleteUsersListByIdDto.getStartId(), deleteUsersListByIdDto.getEndId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("Deleted users: " + deleteUsersListByIdDto.getStartId()
                            + "-" + deleteUsersListByIdDto.getEndId()));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @DeleteMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartIdAsc(@Valid IdDto idDto){
        try{
            userService.deleteListOfUsersByStartIdAsc(idDto.getId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("Users have been deleted from id: " + idDto.getId()));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public User getUserById(@Valid IdDto idDto){

        //log.info("(userController) is authenticated : " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        try{
            return userService.getUserById(idDto.getId());
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
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
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
        catch (MessagingException e) {
            throw new OurMessagingException("Error sending WebSocket message while requested user/all: " + e.getMessage());
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String checkGenderTableAndWelcome(){
        genderService.checkGenderTable();
        return "Hello!" ;
    }

}

