package az.gaming_cafe.exception;

import java.io.Serial;

public class InvalidCredentialsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3L;

    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException() {
        super();
    }
}
