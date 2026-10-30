package org.example.utils.exceptions;

public class InvalidPassword extends RuntimeException {
    public InvalidPassword() {
        super("Password is invalid ");
    }
}
