package com.api.repository;

import com.api.config.enums.Role;
import com.api.entity.Comment;
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
class CommentRepositoryIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CommentRepository commentRepository;

    private User creator;
    private User executor;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp(){

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

        comment = Comment.builder()
                .text("Test task comment text")
                .author(creator)
                .task(task)
                .build();
        commentRepository.save(comment);

    }

    @Test
    void findAllByTaskId() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentsByTaskId = commentRepository.findAllByTaskId(task.getId(), pageable);
        assertEquals(1, commentsByTaskId.getContent().size());
        assertEquals(comment.getText(),commentsByTaskId.getContent().get(0).getText());


    }
}