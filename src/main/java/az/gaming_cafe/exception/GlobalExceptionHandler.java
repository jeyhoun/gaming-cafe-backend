package az.gaming_cafe.exception;

import az.gaming_cafe.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ErrorResponse handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        log.error("ActionLog.InvalidRefreshTokenException: ", ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token Exception");
    }

    @ExceptionHandler(UserInactiveException.class)
    public ErrorResponse handleInvalidRefreshTokenException(UserInactiveException ex) {
        log.error("ActionLog.UserInactiveException: ", ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, "User Inactive Exception");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorResponse handleInvalidCredentialsException(InvalidCredentialsException ex) {
        log.error("ActionLog.InvalidCredentialsException: ", ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid Credentials Exception");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("ActionLog.UserAlreadyExistsException: ", ex);
        return buildResponse(HttpStatus.BAD_REQUEST, "User Already Exists Exception");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException ex) {
        log.error("ActionLog.UserNotFoundException: ", ex);
        return buildResponse(HttpStatus.NOT_FOUND, "User Not Found Exception");
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ErrorResponse handleHttpClientErrorException(HttpClientErrorException.Unauthorized ex) {
        log.error("ActionLog.Unauthorized: ", ex);
        return buildResponse(HttpStatus.UNAUTHORIZED, "Authorization failed. Check your token.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        log.error("ActionLog.MethodArgumentNotValid: ", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    private ErrorResponse buildResponse(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
