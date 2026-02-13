package az.gaming_cafe.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ForgotPasswordRequestDto {

    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    public ForgotPasswordRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
