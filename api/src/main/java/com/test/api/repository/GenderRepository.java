package com.test.api.repository;

import com.test.api.user.Gender;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {

    Gender findByName(String name);
    Gender findByNameIgnoreCase(String name);
}
