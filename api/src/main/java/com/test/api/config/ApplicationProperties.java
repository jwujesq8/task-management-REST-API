//package com.test.api.config;
//
//import com.test.api.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Configuration
//@RequiredArgsConstructor
//public class ApplicationProperties {
//
//    private UserRepository userRepository;
//
//    @Bean
//    public UserDetailsService userDetailsService(){
//        return username -> userRepository.findByLogin(username).orElse(null);
//    }
//}
