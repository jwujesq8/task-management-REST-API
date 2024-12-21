package com.test.api.entity;

import com.test.api.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users_requests_logger")
@Builder
@Getter
@Setter
public class UsersRequestsLogger {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Instant dateTimeUtc;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private User user;

    @NotNull
    private String requestMethod;

    @NotNull
    private String requestPath;

    @Column(columnDefinition = "LONGTEXT")
    private String requestBody;

    @Column(columnDefinition = "LONGTEXT")
    private String responseBody;

}
