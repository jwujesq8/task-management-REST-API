package com.api.service.interfaces;

import com.api.dto.CommentNoIdDto;
import com.api.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

/**
 * Class CommentService
 *
 * Service interface for handling operations related to comments.
 * This interface defines methods for adding comments and retrieving comments associated with a specific task.
 */
public interface CommentService {

    /**
     * Adds a new comment to a specified task.
     *
     * @param taskId The ID of the task to which the comment will be added.
     * @param commentNoIdDto The DTO containing the comment text and author details.
     * @return A DTO representing the added comment, including its ID.
     */
    CommentDto addComment(UUID taskId, CommentNoIdDto commentNoIdDto);

    /**
     * Retrieves all comments associated with a specified task, with pagination support.
     *
     * @param idTask The ID of the task for which to retrieve the comments.
     * @param pageable The pagination information, including page number and page size.
     * @return A page of comments related to the task.
     */
    Page<CommentDto> findAllByTaskId(UUID idTask, Pageable pageable);
}
