package az.gaming_cafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public final class SecurityProperties {

    private final int maxRefreshTokenUse;

    public SecurityProperties(int maxRefreshTokenUse) {
        this.maxRefreshTokenUse = maxRefreshTokenUse;
    }

    public int getMaxRefreshTokenUse() {
        return maxRefreshTokenUse;
    }

}
