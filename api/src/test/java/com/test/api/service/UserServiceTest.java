package com.test.api.service;

import com.test.api.dto.request.POSTUserRequestDto;
import com.test.api.repository.GenderRepository;
import com.test.api.repository.UserRepository;
import com.test.api.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private GenderRepository genderRepository;

    @BeforeEach
    void setUp() {
        User user5 = User.builder()
                .login("opkw6305@gmail.com")
                .password("Hkpf444%2")
                .fullName("Opena Jopena")
                .gender(genderRepository.findByName("female"))
                .build();

        Mockito.when(userRepository.save(user5)).thenReturn(user5);
    }

    @Test
    void addUser() {
        POSTUserRequestDto user5 = new POSTUserRequestDto();
        user5.setLogin("opkw6305@gmail.com");
        user5.setPassword("Hkpf444%2");
        user5.setFullName("Opena Jopena");
        user5.setGenderName("female");

        userService.addUser(user5);
        assertTrue(userRepository.existsByLoginAndPasswordIgnoreCase(
                user5.getLogin(), user5.getPassword()));

    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}