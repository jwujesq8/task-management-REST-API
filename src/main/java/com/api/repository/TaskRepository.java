package com.api.repository;

import com.api.entity.Task;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Hidden
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    void deleteById(UUID id);
    Page<Task> findAllByCreatorId(UUID id, Pageable pageable);
    Page<Task> findAllByExecutorId(UUID id, Pageable pageable);
    boolean existsByIdAndExecutorEmail(UUID id, String email);
}
