package com.test.api.repository;

import com.test.api.user.Gender;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Hidden
@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    Gender findByName(String name);
    Optional<Gender> findByNameIgnoreCase(String name);
}
