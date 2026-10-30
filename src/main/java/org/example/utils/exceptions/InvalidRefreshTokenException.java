package org.example.utils.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException( ) {
        super("Invalid refresh token");
    }
}
