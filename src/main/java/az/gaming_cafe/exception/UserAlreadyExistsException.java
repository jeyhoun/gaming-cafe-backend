package az.gaming_cafe.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
