package com.test.api.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "user", uniqueConstraints = {
    @UniqueConstraint( columnNames = {"password", "login"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    private Long id;

    @NotBlank(message = "login needs to be filled")
    @NotEmpty(message = "login is empty!")
    @Size(
            max = 50,
            message = "Max size for login is 50"
    )
    @Column(unique = true)
    private String login;

    @NotBlank(message = "password needs to be filled")
    @NotEmpty(message = "password is empty!")
    @Size(
            max = 20,
            message = "Max size for password is 20"
    )
    @Column(unique = true)
    private String password;

    @NotBlank(message = "fullName needs to be filled")
    @NotEmpty(message = "fullName is empty!")
    @Size(
            max = 256,
            message = "Max size for full name is 256"
    )
    private String fullName;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    private Gender gender;


}
