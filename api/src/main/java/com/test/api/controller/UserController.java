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
import com.test.api.service.WebSocketNotificationService;
import com.test.api.user.Gender;
import com.test.api.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Locale;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j

@Tag(name="User controller", description="Interaction with users")
public class UserController {

    //private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final GenderService genderService;
//    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketNotificationService webSocketNotificationService;




    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "get user info (login, full name, gender) by id inside the body request")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get user", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (id not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public UserResponseDto getUserById(@Parameter(description = "id inside the body request", required = true, content = @Content(
            schema = @Schema(implementation = IdDto.class, example = "{ \"id\": 1}")))
            @RequestBody @Valid IdDto idDto){

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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "add new user")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful request to add user", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict (user is already exists)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> addUser(@Parameter(description = "postUser(without id, it's auto generated) inside the body request", required = true)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "update user info")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successful request to update user", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (validation error | user not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> updateUser(@Parameter(description = "putUser( with id !!! ) inside the body request", required = true)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete user")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to delete user", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (user not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> deleteUserById(@Parameter(description = "id inside the body request", required = true, content = @Content(
            schema = @Schema(implementation = IdDto.class, example = "{ \"id\": 1}")))
            @RequestBody @Valid IdDto idDto){

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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "get all users list (login, full name, gender)")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get list of users", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "No content (0 users)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public List<UserResponseDto> getAllUsers(){

        try{

            String user_requiter = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            webSocketNotificationService.sendNotification("/topic", user_requiter,"use request GET user/all");
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "(OLD) delete a list of users by start and end id")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to delete list of users", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> deleteListOfUsersById(@Parameter(description = "start and end users id inside the request body", required = true)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "(NEW) delete a list of users by start and end id")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to delete list of users", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartAndEndId(@Parameter(description = "start and end users id inside the request body", required = true)
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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete a list of users by start id (ASCENT)")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to delete list of users", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> deleteListOfUsersByStartIdAsc(@Parameter(description = "start user id inside the request body", required = true)
            @RequestBody @Valid IdDto idDto){

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
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "check if gender table has appropriate content (1-male, 2-female, 3-none)")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to delete list of users", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public ResponseEntity<MessageResponseDto> checkGenderTableAndWelcome(){
        genderService.checkGenderTable();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponseDto("Gender table has an appropriate content"));
    }

}

