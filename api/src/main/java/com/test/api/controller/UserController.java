package com.test.api.controller;

import com.test.api.dto.DeleteUsersListByIdDto;
import com.test.api.dto.IdDto;
import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.UserActionMessageDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.exception.*;
import com.test.api.modelMapper.UserModelMapper;
import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final GenderService genderService;
    private final SimpMessagingTemplate messagingTemplate;




    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    public UserResponseDto getUserById(@RequestBody @Valid IdDto idDto){

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



    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> addUser(
            @RequestBody @Valid POSTUserRequestDto postUserRequestDto){
        try {

            userService.addUser(postUserRequestDto);
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



    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> updateUser(
            @RequestBody @Valid PUTUserRequestDto putUserRequestDto){

        try{

            userService.updateUser(putUserRequestDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto(
                            "User is updated, id: " + putUserRequestDto.getId()));

        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }



    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteUserById(@RequestBody @Valid IdDto idDto){

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



    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<UserResponseDto> getAllUsers(){

        try{
            // TODO: optimize webSocket message
            UserActionMessageDto message = new UserActionMessageDto();
            message.setUser((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            message.setAction("use request GET user/all");
            log.info("message: {}, action: {}", message.getUser(), message.getAction());
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



    @DeleteMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersById(
            @RequestBody @Valid DeleteUsersListByIdDto deleteUsersListByIdDto){

        try{
            Long idCountInRange = userService.deleteListOfUsersById(deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto(idCountInRange + " users have been deleted " +
                            "(provided range: " + deleteUsersListByIdDto.getStartId()
                            + "-" + deleteUsersListByIdDto.getEndId() + ")"));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }



    @DeleteMapping("/list/range")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartAndEndId(
            @RequestBody @Valid DeleteUsersListByIdDto deleteUsersListByIdDto){

        try{

            Long idCountInRange = userService.deleteListOfUsersByStartAndEndId(
                    deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto(idCountInRange + " users have been deleted " +
                            "(provided range: " + deleteUsersListByIdDto.getStartId()
                            + "-" + deleteUsersListByIdDto.getEndId() + ")"));

        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }



    @DeleteMapping("/list/asc")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartIdAsc(@RequestBody @Valid IdDto idDto){

        try{
            Long idCountFrom = userService.deleteListOfUsersByStartIdAsc(idDto.getId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto(idCountFrom + " users have been deleted " +
                            "(provided start id: " + idDto.getId()+ ")"));
        }
        catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
        catch (AuthenticationException e) {
            throw new UserAuthenticationException("User is not authenticated: " + e.getMessage());
        }
    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkGenderTable")
    public ResponseEntity<MessageResponseDto> checkGenderTableAndWelcome(){
        genderService.checkGenderTable();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponseDto("Gender table has an appropriate content"));
    }

}

