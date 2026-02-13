package az.gaming_cafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private int maxRefreshTokenUse;

    public int getMaxRefreshTokenUse() {
        return maxRefreshTokenUse;
    }

    public void setMaxRefreshTokenUse(int maxRefreshTokenUse) {
        this.maxRefreshTokenUse = maxRefreshTokenUse;
    }
}
