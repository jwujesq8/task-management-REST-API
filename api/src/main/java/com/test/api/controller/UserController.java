package com.test.api.controller;

import com.test.api.dto.DeleteUsersListByIdDto;
import com.test.api.dto.IdDto;
import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.dto.request.PUTUserRequestDto;
import com.test.api.dto.response.MessageResponseDto;
import com.test.api.dto.response.UserResponseDto;
import com.test.api.service.GenderService;
import com.test.api.service.UserService;
import com.test.api.service.WebSocketNotificationService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j

@Tag(name="User controller", description="Interaction with users")
public class UserController {

    private final UserService userService;
    private final GenderService genderService;
    private final WebSocketNotificationService webSocketNotificationService;




    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "get user info (login, full name, gender) by id inside the body request")
    @SecurityRequirement(name = "JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (id not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public UserResponseDto getUserById(@Parameter(description = "id inside the body request", required = true, content = @Content(
            schema = @Schema(implementation = IdDto.class, example = "{ \"id\": 1}")))
            @RequestBody @Valid IdDto idDto){

        return userService.getUserById(idDto.getId());
    }



    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "add new user")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (validation error)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflict (user is already exists)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public UserResponseDto addUser(@Parameter(description = "postUser(without id, it's auto generated) inside the body request", required = true)
            @RequestBody @Valid POSTUserRequestDto postUserRequestDto){

        return userService.addUser(postUserRequestDto);
    }



    @PutMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "update user info")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (validation error | user not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public UserResponseDto updateUser(@Parameter(description = "putUser( with id !!! ) inside the body request", required = true)
            @RequestBody @Valid PUTUserRequestDto putUserRequestDto){

            return userService.updateUser(putUserRequestDto);
    }



    @DeleteMapping("")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete user")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (user not found)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public UserResponseDto deleteUserById(@Parameter(description = "id inside the body request", required = true, content = @Content(
            schema = @Schema(implementation = IdDto.class, example = "{ \"id\": 1}")))
            @RequestBody @Valid IdDto idDto){

            return userService.deleteUser(idDto.getId());
    }



    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "get all users list (login, full name, gender)")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public List<UserResponseDto> getAllUsers(){

            String userRequiter = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            webSocketNotificationService.sendNotification("/topic", userRequiter,"use request GET user/all");
            return userService.getAllUsers();

    }



    @DeleteMapping("/list")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "(OLD) delete a list of users by start and end id")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public MessageResponseDto deleteListOfUsersById(@Parameter(description = "start and end users id inside the request body", required = true)
            @RequestBody @Valid DeleteUsersListByIdDto deleteUsersListByIdDto){

            Long idCountInRange = userService.deleteListOfUsersById(deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return new MessageResponseDto(idCountInRange + " users have been deleted " +
                    "(provided range: " + deleteUsersListByIdDto.getStartId()
                    + "-" + deleteUsersListByIdDto.getEndId() + ")");

    }



    @DeleteMapping("/list/range")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "(NEW) delete a list of users by start and end id")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public MessageResponseDto deleteListOfUsersByStartAndEndId(@Parameter(description = "start and end users id inside the request body", required = true)
            @RequestBody @Valid DeleteUsersListByIdDto deleteUsersListByIdDto){

            Long idCountInRange = userService.deleteListOfUsersByStartAndEndId(
                    deleteUsersListByIdDto.getStartId(),deleteUsersListByIdDto.getEndId());
            return new MessageResponseDto(idCountInRange + " users have been deleted " +
                    "(provided range: " + deleteUsersListByIdDto.getStartId()
                    + "-" + deleteUsersListByIdDto.getEndId() + ")");


    }



    @DeleteMapping("/list/asc")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "delete a list of users by start id (ASCENT)")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "Bad request (0 users with id in provided range)", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public MessageResponseDto deleteListOfUsersByStartIdAsc(@Parameter(description = "start user id inside the request body", required = true)
            @RequestBody @Valid IdDto idDto){

            Long idCountFrom = userService.deleteListOfUsersByStartIdAsc(idDto.getId());
            return new MessageResponseDto(idCountFrom + " users have been deleted " +
                    "(provided start id: " + idDto.getId()+ ")");

    }



    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkGenderTable")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "check if gender table has appropriate content (1-male, 2-female, 3-none)")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "Non valid token", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Authentication error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error or db error", content = @Content(schema = @Schema(implementation = MessageResponseDto.class)))
    }
    )
    public MessageResponseDto checkGenderTableAndWelcome(){

        genderService.checkGenderTable();
        return new MessageResponseDto("Gender table has an appropriate content");
    }

}

