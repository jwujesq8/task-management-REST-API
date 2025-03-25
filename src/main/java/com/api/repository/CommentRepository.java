package com.api.repository;

import com.api.entity.Comment;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Hidden
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> getByTaskId(UUID idTask);
}
