package az.gaming_cafe.model.dto.response;

import java.time.Instant;
import java.util.Objects;

public class SignInResponseDto {

    private Long id;
    private String username;
    private String email;
    private String token;
    private Instant expiresAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public SignInResponseDto() {
    }

    public SignInResponseDto(Long id, String username, String email, String token, Instant expiresAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SignInResponseDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(token, that.token) && Objects.equals(expiresAt, that.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, token, expiresAt);
    }

    @Override
    public String toString() {
        return "SignInResponseDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
