package az.gaming_cafe.exception;

import java.io.Serial;

public class InvalidRefreshTokenException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5L;

    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException() {
        super();
    }
}
