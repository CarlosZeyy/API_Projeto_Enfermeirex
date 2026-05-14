package dev.carlosmoises.projeto.enferm.config;

public class TokenExpiredException extends RuntimeException {
    private final String message;

    public TokenExpiredException(String message) {
        this.message = message;
    }
}
