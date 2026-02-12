package az.gaming_cafe.exception.data;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // AUTH
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
    USER_INACTIVE(HttpStatus.UNAUTHORIZED, "User account is inactive"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid refresh token"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    TOKEN_ALREADY_USED(HttpStatus.UNAUTHORIZED, "Token already used"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
    TOKEN_REUSE_DETECTED(HttpStatus.UNAUTHORIZED, "Token reuse detected"),

    // USER
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "User already exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "User role not found"),

    // VALIDATION
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),

    // SECURITY
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authorization failed"),

    // GENERIC
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),

    //COMPUTER
    COMPUTER_NOT_FOUND(HttpStatus.NOT_FOUND,"Computer not found");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

