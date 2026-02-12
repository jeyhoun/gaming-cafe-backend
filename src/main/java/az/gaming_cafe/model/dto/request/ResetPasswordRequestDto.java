package az.gaming_cafe.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ResetPasswordRequestDto {

    @NotNull(message = "Token is required")
    @NotBlank(message = "Token is required")
    private String token;

    @NotNull(message = "New password is required")
    @NotBlank(message = "New password is required")
    private String newPassword;

    public ResetPasswordRequestDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
