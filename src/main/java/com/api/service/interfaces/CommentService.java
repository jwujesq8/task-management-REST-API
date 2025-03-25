package com.api.service.interfaces;

import com.api.dto.IdDto;
import com.api.dto.CommentNoIdDto;
import com.api.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {

    CommentDto addComment(CommentNoIdDto commentNoIdDto);
    List<CommentDto> getTasksCommentsListById(IdDto idTask);
}
