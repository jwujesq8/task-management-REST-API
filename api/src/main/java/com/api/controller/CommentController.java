package com.api.controller;

import com.api.dto.CommentDto;
import com.api.dto.CommentNoIdDto;
import com.api.service.interfaces.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@Tag(name="Comment controller", description="Interaction with tasks comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    @Operation(summary = "post a new comment to a task by task id (only for admin and executor)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "New comment is posted", content = @Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated)",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID taskId,
                                                 @Valid @RequestBody CommentNoIdDto commentNoIdDto) {
        return ResponseEntity.ok(commentService.addComment(taskId, commentNoIdDto));
    }


    @GetMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    @Operation(summary = "get tasks comments by task id (only for admin and executor)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful request to get tasks comments", content = @Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated)",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<Page<CommentDto>> getTaskComments(@PathVariable UUID taskId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(commentService.findAllByTaskId(taskId, PageRequest.of(page,size)));
    }
}
