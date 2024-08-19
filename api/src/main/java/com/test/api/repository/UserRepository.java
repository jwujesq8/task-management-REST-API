package com.test.api.repository;


import com.test.api.user.Gender;
import com.test.api.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM user u WHERE u.id>=?1 AND u.id<=?2;",
            nativeQuery = true
    )
    void deleteListOfUsersById(Long startId, Long endId);

    List<User> findByGender(Optional<Gender> gender);
    Optional<User> findByLogin(String login);

    boolean existsByLoginAndPasswordIgnoreCase(String login, String password);

    boolean existsById(Long id);

}
