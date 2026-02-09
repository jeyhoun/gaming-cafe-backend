package az.gaming_cafe.model.dto.common;

import java.time.LocalDateTime;
import java.util.Objects;

public class ApiResult<T> {

    private int code;
    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResult<T> ok(T data) {
        return ApiResult.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResult<T> of(T data) {
        return data != null ? ok(data) : ok(null);
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return ApiResult.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResult<T> error(String message) {
        return ApiResult.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    private ApiResult(Builder<T> builder) {
        this.data = builder.data;
        this.message = builder.message;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();

        if (builder.success) {
            this.code = 200;
            this.message = "OK";
            this.status = "SUCCESS";
        } else {
            this.code = 500;
            this.message = "Internal Server Error";
            this.status = "FAIL";
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private boolean success;
        private String message;
        private T data;
        private LocalDateTime timestamp;

        public Builder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResult<T> build() {
            return new ApiResult<>(this);
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApiResult<?> that)) return false;
        return code == that.code && Objects.equals(status, that.status)
                && Objects.equals(message, that.message)
                && Objects.equals(data, that.data)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, status, message, data, timestamp);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }
}
