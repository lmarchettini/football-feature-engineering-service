package com.footballai.engineering.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FeatureEngineeringRunResponse(

        String status,

        int settledFeatures,

        int generatedTrainingFeatures,

        int generatedUpcomingFeatures,

        int failedFeatures,

        int batchSize,

        int upcomingDays,

        LocalDateTime startedAt,

        LocalDateTime completedAt,

        List<String> errors

) {
}