package az.gaming_cafe.listener.event;

public class UserActionEvent {

    private final Long userId;
    private final String action;
    private final String status;
    private final String errorMessage;
    private final String ipAddress;
    private final String userAgent;

    private UserActionEvent(Builder builder) {
        this.userId = builder.userId;
        this.action = builder.action;
        this.status = builder.status;
        this.errorMessage = builder.errorMessage;
        this.ipAddress = builder.ipAddress;
        this.userAgent = builder.userAgent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getUserId() {
        return userId;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {

        private Long userId;
        private String action;
        private String status;
        private String errorMessage;
        private String ipAddress;
        private String userAgent;

        private Builder() {
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public UserActionEvent build() {
            return new UserActionEvent(this);
        }
    }
}

