package com.api.repository;

import com.api.entity.Task;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Hidden
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    void deleteById(UUID id);
    List<Task> findAll();
    List<Task> findAllByCreatorId(UUID id);
    List<Task> findAllByExecutorId(UUID id);
    boolean existsByIdAndExecutorFullName(UUID id, String fullName);
}
