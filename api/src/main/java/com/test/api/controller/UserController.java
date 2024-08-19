package com.test.api.controller;

import com.test.api.dto.DeleteUsersListByIdDto;
import com.test.api.dto.IdDto;
import com.test.api.dto.request.UserRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.UserActionMessageDto;
import com.test.api.exception.BadClientRequestException;
import com.test.api.exception.IdNotFoundException;
import com.test.api.exception.OurServiceErrorException;
import com.test.api.exception.ValidException;
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
    private final UserModelMapper userModelMapper;


    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> addUser(@RequestBody @NotNull UserRequestDto userRequestDto) throws ConstraintViolationException {
        try {
            User user = userModelMapper.convert_UserRequestDto_to_User(userRequestDto);
            userService.addUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is saved"));
        }
        catch (ValidException valEx){
            throw valEx;
        }
        catch (ServerErrorException serverErrEx){
            throw new OurServiceErrorException("Server error while adding new user. " + serverErrEx.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> updateUser(@NotNull IdDto idDto, @Valid @RequestBody @NotNull UserRequestDto userRequestDto){

        try{

            if(userRequestDto.getId()==null) {userRequestDto.setId(idDto.getId());}

            User user = userModelMapper.convert_UserRequestDto_to_User(userRequestDto);
            userService.updateUser(idDto.getId(), user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("User is updated, id: " + user.getId()));

        }
        catch (BadClientRequestException badClReqEx){
            throw new BadClientRequestException("Bad user request to update user info");
        }
        catch (ServerErrorException serverErrEx){
            throw new OurServiceErrorException("Server error with updating user by id. " +
                    serverErrEx.getMessage());
        }
        catch (ValidException valEx){
            throw valEx;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteUserById(@NotNull IdDto idDto){

        try {
            userService.deleteUser(idDto.getId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("User is removed, id: " + idDto.getId()));
        }
        catch (ServerErrorException serverErrEx){
            throw new OurServiceErrorException("Server error with deleting user by id. " +
                    serverErrEx.getMessage());
        }
    }

    @DeleteMapping("/{startId}/{endId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponseDto> deleteListOfUsersById(@NotNull @Valid DeleteUsersListByIdDto deleteUsersListByIdDto){
        try{
            userService.deleteListOfUsersById(deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new MessageResponseDto("Deleted users: " + deleteUsersListByIdDto.getStartId()
                            + "-" + deleteUsersListByIdDto.getEndId()));
        }
        catch (ServerErrorException serverErrEx) {
            throw new OurServiceErrorException("Server error with deleting list of users. " + serverErrEx.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Optional<User> getUserById(@NotNull IdDto idDto){

        //log.info("(userController) is authenticated : " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        try{
            return userService.getUserById(idDto.getId());
        }
        catch (ServerErrorException serverErrEx){
            throw new OurServiceErrorException("Server error while getting user bu id. " +
                    serverErrEx.getMessage());
        }
        catch (HttpClientErrorException httpClErrEx){
            throw new BadClientRequestException("Bad client request to get user info by id. " +
                    httpClErrEx.getMessage());
        }
        catch (IdNotFoundException idNotFoundException){
            throw new IdNotFoundException(idNotFoundException.getMessage());
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
            throw new OurServiceErrorException("Server error while getting all users. " +
                    serverErrEx.getMessage());
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public String checkGenderTableAndWelcome() throws AuthenticationException {
        genderService.checkGenderTable();
        return "Hello!" ;
    }

}

