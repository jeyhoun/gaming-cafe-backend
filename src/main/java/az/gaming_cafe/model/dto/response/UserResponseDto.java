package az.gaming_cafe.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public UserResponseDto() {
    }

    private UserResponseDto(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.roles = builder.roles;
        this.createdAt = builder.createdAt;
        this.lastLoginAt = builder.lastLoginAt;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }

    public static Builder builder() { return new Builder(); }

    @SuppressWarnings("checkstyle:HiddenField")
    public static class Builder {
        private Long id;
        private String username;
        private String email;
        private List<String> roles;
        private LocalDateTime createdAt;
        private LocalDateTime lastLoginAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder roles(List<String> roles) { this.roles = roles; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder lastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }

        public UserResponseDto build() { return new UserResponseDto(this); }
    }
}

