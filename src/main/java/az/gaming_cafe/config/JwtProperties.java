package az.gaming_cafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secretKey,
        long refreshTokenExpiration,
        long accessTokenExpiration,
        long maxRefreshTokenUse) {}
