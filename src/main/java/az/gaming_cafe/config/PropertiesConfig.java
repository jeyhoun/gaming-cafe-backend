package az.gaming_cafe.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        AppProperties.class,
        JwtProperties.class
})
@Configuration
public class PropertiesConfig {
}
