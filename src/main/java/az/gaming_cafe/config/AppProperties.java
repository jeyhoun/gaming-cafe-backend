package az.gaming_cafe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;

@ConfigurationProperties(prefix = "app")
public final class AppProperties {

    private final String frontendUrl;

    public AppProperties(String frontendUrl) {
        this.frontendUrl = frontendUrl;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }
}
