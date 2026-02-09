package az.gaming_cafe.exception;

import java.io.Serial;

public class UserInactiveException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4L;

    public UserInactiveException(String message) {
        super(message);
    }

    public UserInactiveException() {
        super();
    }
}
