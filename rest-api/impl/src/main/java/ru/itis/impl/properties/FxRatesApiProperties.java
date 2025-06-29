package ru.itis.impl.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "fxrates")
public class FxRatesApiProperties {
    private String apiKey;
    private String baseUrl;
}