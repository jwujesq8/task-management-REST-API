package com.api.service;

import com.api.dto.IdDto;
import com.api.dto.CommentNoIdDto;
import com.api.dto.CommentDto;
import com.api.entity.Comment;
import com.api.repository.CommentRepository;
import com.api.service.interfaces.CommentService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
//    private final CommentModelMapper commentModelMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public CommentDto addComment(CommentNoIdDto commentNoIdDto) {
        Comment comment = commentRepository.save(modelMapper.map(commentNoIdDto, Comment.class));
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public List<CommentDto> getTasksCommentsListById(IdDto idTask) {
        List<Comment> comments= commentRepository.getByTaskId(idTask.id());
        return comments.stream().map(comment -> modelMapper.map(comment,CommentDto.class)).toList();
    }
}
