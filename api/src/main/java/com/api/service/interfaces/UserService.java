package com.api.service.interfaces;

import com.api.entity.User;
import java.util.Optional;

/**
 * Class UserService
 *
 * Service interface for handling operations related to users.
 * This interface defines methods for user-related actions such as retrieving user details.
 */
public interface UserService {

    /**
     * Retrieves a user by their email (login).
     *
     * @param login The email (login) of the user to be retrieved.
     * @return An {@link Optional} containing the user if found, or an empty {@link Optional} if no user with the specified email exists.
     */
    Optional<User> getUserByEmail(String login);
}
