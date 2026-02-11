package az.gaming_cafe.exception;

import az.gaming_cafe.exception.data.ErrorCode;

public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }

    public ApplicationException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
