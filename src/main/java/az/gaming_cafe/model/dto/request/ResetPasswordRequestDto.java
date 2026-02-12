package az.gaming_cafe.model.dto.request;

public class ResetPasswordRequestDto {

    private String token;
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
