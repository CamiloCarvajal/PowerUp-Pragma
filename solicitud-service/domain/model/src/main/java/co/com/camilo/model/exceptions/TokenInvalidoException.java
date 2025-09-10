package co.com.camilo.model.exceptions;

public class TokenInvalidoException extends RuntimeException {
    
    public TokenInvalidoException(String message) {
        super(message);
    }
    
    public TokenInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}

