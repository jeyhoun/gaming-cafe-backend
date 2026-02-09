package az.gaming_cafe.model.dto.response;

public class RefreshTokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long expiresIn;

    public static Builder builder() {
        return new Builder();
    }

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

    public RefreshTokenResponseDto() {
    }

    public RefreshTokenResponseDto(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}