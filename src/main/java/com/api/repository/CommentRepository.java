package com.api.repository;

import com.api.entity.Comment;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Class CommentRepository
 *
 * Repository interface for performing CRUD operations on the Comment entity.
 * This interface extends JpaRepository to provide standard database operations and custom queries.
 */
@Hidden
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    /**
     * Deletes all comments associated with a specific task.
     *
     * @param idTask The UUID of the task for which comments need to be deleted.
     */
    void deleteByTaskId(UUID idTask);

    /**
     * Retrieves all comments associated with a specific task, with pagination support.
     *
     * @param idTask The UUID of the task for which comments are to be retrieved.
     * @param pageable The pagination information.
     * @return A Page containing the comments for the specified task.
     */
    Page<Comment> findAllByTaskId(UUID idTask, Pageable pageable);
}
