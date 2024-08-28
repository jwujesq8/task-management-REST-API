package com.test.api.repository;

import com.test.api.entity.UsersRequestsLogger;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface UsersRequestsLoggerRepository extends JpaRepository<UsersRequestsLogger, Long> {



}
