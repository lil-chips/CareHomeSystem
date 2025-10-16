package edu.rmit.cosc1295.carehome;

/**
 * Exception thrown when a staff member tries to perform an action
 * they are not authorized to do.
 */

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
