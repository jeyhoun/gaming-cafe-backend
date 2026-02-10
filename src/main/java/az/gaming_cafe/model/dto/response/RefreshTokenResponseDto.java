package az.gaming_cafe.model.dto.response;

public class RefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public RefreshTokenResponseDto() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private String accessToken;
        private String refreshToken;
        private Long expiresIn;

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public RefreshTokenResponseDto build() {
            RefreshTokenResponseDto dto = new RefreshTokenResponseDto();
            dto.accessToken = this.accessToken;
            dto.refreshToken = this.refreshToken;
            dto.expiresIn = this.expiresIn;
            return dto;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
