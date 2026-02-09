package az.gaming_cafe.model.dto.response;

import java.util.Objects;

public class SignInResponseDto {

    private Long id;
    private String username;
    private String email;
    private String token;

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

    public SignInResponseDto() {
    }

    public SignInResponseDto(Long id, String username, String email, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.token = token;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private String token;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public SignInResponseDto build() {
            return new SignInResponseDto(id, username, email, token);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SignInResponseDto that)) return false;
        return Objects.equals(id, that.id)
                && Objects.equals(username, that.username)
                && Objects.equals(email, that.email)
                && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, token);
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