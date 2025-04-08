package com.api.repository;

import com.api.entity.Task;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * Class TaskRepository
 *
 * Repository interface for performing CRUD operations on the Task entity.
 * This interface extends JpaRepository to provide standard database operations and custom queries.
 */
@Hidden
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    /**
     * Deletes a task by its ID.
     *
     * @param id The UUID of the task to be deleted.
     */
    void deleteById(UUID id);

    /**
     * Retrieves a paginated list of tasks created by a specific user.
     *
     * @param id The UUID of the user who created the tasks.
     * @param pageable The pagination information.
     * @return A Page containing the tasks created by the specified user.
     */
    Page<Task> findAllByCreatorId(UUID id, Pageable pageable);

    /**
     * Retrieves a paginated list of tasks assigned to a specific executor.
     *
     * @param id The UUID of the executor.
     * @param pageable The pagination information.
     * @return A Page containing the tasks assigned to the specified executor.
     */
    Page<Task> findAllByExecutorId(UUID id, Pageable pageable);

    /**
     * Checks whether a task with a specific ID exists and is assigned to an executor with a given email.
     *
     * @param id The UUID of the task.
     * @param email The email of the executor.
     * @return true if the task exists and is assigned to the executor, otherwise false.
     */
    boolean existsByIdAndExecutorEmail(UUID id, String email);
}
