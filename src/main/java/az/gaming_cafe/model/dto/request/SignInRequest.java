package az.gaming_cafe.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class SignInRequest {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
