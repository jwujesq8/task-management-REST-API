package com.api.service;

import com.api.dto.IdDto;
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

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    @Override
    public CommentDto addComment(UUID taskId, CommentNoIdDto commentNoIdDto) {
        Comment comment = modelMapper.map(commentNoIdDto, Comment.class);
        comment.setTask(taskRepository.getById(taskId));
        commentRepository.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public Page<CommentDto> findAllByTaskId(UUID idTask, Pageable pageable) {
        return commentRepository.findAllByTaskId(idTask, pageable)
                .map(comment -> modelMapper.map(comment, CommentDto.class));
    }
}
