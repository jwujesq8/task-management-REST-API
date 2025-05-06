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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Class CommentController
 *
 * Controller for managing comments related to tasks.
 * Provides endpoints for posting new comments and retrieving comments by task ID.
 */
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@Tag(name="Comment controller", description="Interaction with tasks comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * Endpoint to add a new comment to a task.
     * Accessible only by users with the ADMIN role or the task executor.
     *
     * @param taskId The ID of the task to add the comment to.
     * @param commentNoIdDto The request body containing the comment details (excluding the ID).
     * @return {@link CommentDto} containing the newly posted comment.
     */
    @PostMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    @Operation(summary = "post a new comment to a task by task id (only for admin and executor)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New comment is posted", content = @Content(schema = @Schema(implementation = CommentDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden (non authenticated)",  content = @Content(mediaType = "none"))}
    )
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID taskId,
                                                 @Valid @RequestBody CommentNoIdDto commentNoIdDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.addComment(taskId, commentNoIdDto));
    }

    /**
     * Endpoint to retrieve all comments associated with a specific task.
     * Accessible only by users with the ADMIN role or the task executor.
     *
     * @param taskId The ID of the task whose comments are to be retrieved.
     * @param page The page number for pagination.
     * @param size The number of comments per page.
     * @return A {@link Page<CommentDto>} containing the comments for the specified task.
     */
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
