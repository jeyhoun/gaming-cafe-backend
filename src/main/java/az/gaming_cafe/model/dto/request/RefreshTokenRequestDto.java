package az.gaming_cafe.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class RefreshTokenRequestDto {

    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenRequestDto() {
    }

    public RefreshTokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
