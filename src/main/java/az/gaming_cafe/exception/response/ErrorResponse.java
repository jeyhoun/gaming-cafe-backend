package az.gaming_cafe.exception.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> details;
    private Map<String, String> validationErrors;
    private String path;

    private ErrorResponse(Builder builder) {
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
        this.details = builder.details;
        this.validationErrors = builder.validationErrors;
        this.path = builder.path;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int status;
        private String path;
        private String error;
        private String message;
        private LocalDateTime timestamp;
        private Map<String, String> details;
        private Map<String, String> validationErrors;

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder details(Map<String, String> details) {
            this.details = details;
            return this;
        }

        public Builder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

    public int getStatus() {
        return status;
    }

    public String getPath() {
        return path;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
