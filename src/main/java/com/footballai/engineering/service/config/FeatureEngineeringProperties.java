package com.footballai.engineering.service.config;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "feature-engineering")
public class FeatureEngineeringProperties {

    private int lookbackMatches = 5;

    public int getLookbackMatches() {
        return lookbackMatches;
    }

    public void setLookbackMatches(int lookbackMatches) {
        this.lookbackMatches = lookbackMatches;
    }
}