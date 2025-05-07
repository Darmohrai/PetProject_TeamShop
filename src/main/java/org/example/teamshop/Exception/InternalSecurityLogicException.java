package org.example.teamshop.Exception;

public class InternalSecurityLogicException extends RuntimeException {
    public InternalSecurityLogicException(String message) {
        super(message);
    }

    public InternalSecurityLogicException(String message, Throwable cause) {
      super(message, cause);
    }
}
