package com.api.service;

import com.api.dto.CommentNoIdDto;
import com.api.dto.CommentDto;
import com.api.entity.Comment;
import com.api.repository.CommentRepository;
import com.api.repository.TaskRepository;
import com.api.service.interfaces.CommentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Class CommentServiceImpl
 *
 * Service implementation for handling operations related to comments, including adding and retrieving comments by task ID.
 */
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    /**
     * Adds a new comment to a task.
     *
     * @param taskId The ID of the task to associate the comment with.
     * @param commentNoIdDto The DTO containing the comment data (without ID).
     * @return A {@link CommentDto} representing the saved comment.
     */
    @Override
    public CommentDto addComment(UUID taskId, CommentNoIdDto commentNoIdDto) {
        Comment comment = modelMapper.map(commentNoIdDto, Comment.class);
        comment.setTask(taskRepository.getById(taskId));
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    /**
     * Retrieves all comments associated with a specific task, with pagination support.
     *
     * @param idTask The ID of the task whose comments are to be retrieved.
     * @param pageable The pagination information (e.g., page number, size).
     * @return A {@link Page} of {@link CommentDto} representing the comments for the given task.
     */
    @Override
    public Page<CommentDto> findAllByTaskId(UUID idTask, Pageable pageable) {
        return commentRepository.findAllByTaskId(idTask, pageable)
                .map(comment -> modelMapper.map(comment, CommentDto.class));
    }
}
