package com.test.api.repository;

import com.test.api.user.Gender;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Hidden
@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    Gender findByName(String name);
    Gender findByNameIgnoreCase(String name);
}
