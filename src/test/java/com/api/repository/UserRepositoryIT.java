package com.api.repository;

import com.api.config.Role;
import com.api.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp(){
        user = User.builder()
                .fullName("Name Surname")
                .email("user@gmail.com")
                .password("dsf789ert")
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Test
    void findByEmail() {

        Optional<User> userDB = userRepository.findByEmail(user.getEmail());
        assertTrue(userDB.isPresent());
        assertEquals(userDB.get().getPassword(), user.getPassword());
    }
}