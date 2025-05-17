package com.api.repository;


import com.api.config.enums.Role;
import com.api.entity.Task;
import com.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryIT {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    private Task task;
    private User creator;
    private User executor;

    @BeforeEach
    void setUp() {
        creator = User.builder()
                .fullName("Name Surname")
                .email("creator@gmail.com")
                .password("dsf789ert")
                .role(Role.ADMIN)
                .build();

        executor = User.builder()
                .fullName("Name Surname")
                .email("executor@example.com")
                .password("dsf789ert")
                .role(Role.ADMIN)
                .build();


        userRepository.save(executor);
        userRepository.save(creator);

        task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .status("pending")
                .priority("mid")
                .creator(creator)
                .executor(executor)
                .build();

        taskRepository.save(task);
    }

    @Test

    void deleteById() {
        taskRepository.deleteById(task.getId());
        assertFalse(taskRepository.findById(task.getId()).isPresent());

    }

    @Test
    void findAllByCreatorId() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = taskRepository.findAllByCreatorId(creator.getId(), pageable);

        assertFalse(tasks.getContent().isEmpty());
        assertEquals(1, tasks.getContent().size());
        assertEquals(creator.getId(), tasks.getContent().get(0).getCreator().getId());
    }

    @Test
    void findAllByExecutorId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> tasks = taskRepository.findAllByExecutorId(executor.getId(), pageable);

        assertFalse(tasks.getContent().isEmpty());
        assertEquals(1, tasks.getContent().size());
        assertEquals(executor.getId(),tasks.getContent().get(0).getExecutor().getId());

    }

    @Test
    void existsByIdAndExecutorEmail() {
        assertTrue(taskRepository.existsByIdAndExecutorEmail(task.getId(), executor.getEmail()));
    }
}