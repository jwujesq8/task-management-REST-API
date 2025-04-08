package com.api.service.interfaces;

import com.api.dto.CommentNoIdDto;
import com.api.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CommentService {

    CommentDto addComment(UUID taskId, CommentNoIdDto commentNoIdDto);
    Page<CommentDto> findAllByTaskId(UUID idTask, Pageable pageable);
}
