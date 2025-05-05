package com.api.exception;

/**
 * Class ForbiddenException
 *
 * Custom exception class for handling forbidden errors.
 * This exception is thrown when forbidden-related issues occur,
 * such as role permission.
 */
public class ForbiddenException extends RuntimeException {
    /**
     * Constructor for creating a new instance of ForbiddenException.
     *
     * @param errorMessage The message that explains the error.
     */
    public ForbiddenException(String errorMessage) {
        super(errorMessage);
    }
}