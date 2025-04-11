package org.example.teamshop.Exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class JwtTokenExpiredException extends RuntimeException {
    Instant expires;

    public JwtTokenExpiredException(String message, Instant expires) {
        super(message + ": " + expires);
        this.expires = expires;
    }
}
