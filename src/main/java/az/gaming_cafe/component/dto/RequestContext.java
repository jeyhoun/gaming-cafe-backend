package az.gaming_cafe.component.dto;

public class RequestContext {

    private String ipAddress;
    private String userAgent;

    public RequestContext() {
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public static RequestContext.Builder builder() {
        return new RequestContext.Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private String ipAddress;
        private String userAgent;

        public RequestContext.Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }

        public RequestContext.Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public RequestContext build() {
            RequestContext dto = new RequestContext();
            dto.ipAddress = this.ipAddress;
            dto.userAgent = this.userAgent;
            return dto;
        }
    }
}
