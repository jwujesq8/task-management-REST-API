package com.api.entity;

import com.api.config.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Class User
 *
 * Entity class representing a user in the system.
 * This class implements the {@link UserDetails} interface to provide user-specific data to Spring Security.
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User implements UserDetails {

    /**
     * The unique identifier of the user.
     * This is a UUID that uniquely identifies the user.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * The full name of the user.
     * This field contains the user's full name.
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * The email of the user.
     * This email serves as the unique identifier for login purposes.
     */
    private String email;

    /**
     * The password of the user.
     * This is the user's password for authentication.
     */
    private String password;

    /**
     * The role of the user.
     * This field determines the user's access rights and privileges within the system.
     *
     * @see Role
     */
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(email));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
