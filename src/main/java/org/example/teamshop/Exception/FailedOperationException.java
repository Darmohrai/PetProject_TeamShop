package org.example.teamshop.Exception;

public class FailedOperationException extends RuntimeException {
    public FailedOperationException(String message) {
        super(message);
    }
}
