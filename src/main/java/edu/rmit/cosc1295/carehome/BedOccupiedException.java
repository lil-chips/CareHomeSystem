package edu.rmit.cosc1295.carehome;

/**
 * When trying to move to a bed that is occupied
 * then show error message
 */

public class BedOccupiedException extends RuntimeException{

    public BedOccupiedException(String message) {
        super(message);
    }
}
