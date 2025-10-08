package edu.rmit.cosc1295.carehome;

/**
 * When a staff is attempting an action while not rostered at that time
 * It will show error message
 */

public class NotWorkingException extends RuntimeException {

    public NotWorkingException(String message) {
        super(message);
    }
}
