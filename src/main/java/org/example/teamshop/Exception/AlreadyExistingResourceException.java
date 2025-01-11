package org.example.teamshop.Exception;

public class AlreadyExistingResourceException extends RuntimeException {
    public AlreadyExistingResourceException(String message) {
        super(message);
    }
}
