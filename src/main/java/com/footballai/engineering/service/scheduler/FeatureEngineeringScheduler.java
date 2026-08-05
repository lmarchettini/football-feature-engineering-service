package com.footballai.engineering.service.scheduler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.footballai.engineering.service.service.FeatureEngineeringService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(
        prefix = "feature-engineering.scheduling",
        name = "enabled",
        havingValue = "true"
)
public class FeatureEngineeringScheduler {

    private final FeatureEngineeringService featureEngineeringService;

    @Scheduled(cron = "${feature-engineering.cron}")
    public void generateFeatures() {
        log.debug("Running feature engineering scheduler");
        featureEngineeringService.generateFeatures();
    }
}