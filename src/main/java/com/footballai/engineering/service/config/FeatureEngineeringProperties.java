package com.footballai.engineering.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "feature-engineering")
public class FeatureEngineeringProperties {

    private int lookbackMatches = 5;

    private int batchSize = 500;

    private String featureVersion = "v4";
}