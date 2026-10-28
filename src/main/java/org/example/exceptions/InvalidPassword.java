package org.example.exceptions;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword() {
        super("Password is invalid ");
    }
}
