package az.gaming_cafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties(prefix = "jwt")
public final class JwtProperties {

    private final String secretKey;
    private final long refreshTokenExpiration;
    private final long accessTokenExpiration;

    public JwtProperties(String secretKey,
                         long refreshTokenExpiration,
                         long accessTokenExpiration) {
        this.secretKey = secretKey;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }
}
