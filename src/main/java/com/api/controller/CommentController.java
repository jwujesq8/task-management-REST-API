package com.api.controller;

import com.api.dto.CommentDto;
import com.api.dto.CommentNoIdDto;
import com.api.dto.IdDto;
import com.api.service.interfaces.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID taskId,
                                                 @Valid @RequestBody CommentNoIdDto commentNoIdDto) {
        return ResponseEntity.ok(commentService.addComment(taskId, commentNoIdDto));
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("isAuthenticated() && " +
            "(hasRole('ADMIN') || @taskPermissionChecker.isTaskExecutor(#taskId, authentication.principal))")
    public ResponseEntity<Page<CommentDto>> getTaskComments(@PathVariable UUID taskId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "3") int size) {
        return ResponseEntity.ok(commentService.findAllByTaskId(taskId, PageRequest.of(page,size)));
    }
}
