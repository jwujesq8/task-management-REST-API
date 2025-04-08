package com.api.service;

import com.api.entity.User;
import com.api.repository.UserRepository;
import com.api.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Override
    public Optional<User> getUserByEmail(String login) {
        return userRepository.findByEmail(login);
    }
}
