package com.footballai.engineering.service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.footballai.engineering.service.config.FeatureEngineeringProperties;
import com.footballai.engineering.service.dto.FeatureEngineeringRunRequest;
import com.footballai.engineering.service.dto.FeatureEngineeringRunResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManualFeatureEngineeringService {

    private final FeatureEngineeringService
            featureEngineeringService;

    private final FeatureEngineeringProperties
            properties;

    public FeatureEngineeringRunResponse run(
            FeatureEngineeringRunRequest request
    ) {

        if (!request.shouldSettleUpcoming()
                && !request.shouldGenerateTraining()
                && !request.shouldGenerateUpcoming()) {

            throw new IllegalArgumentException(
                    "Select at least one feature engineering operation"
            );
        }

        int batchSize =
                request.batchSize() != null
                        ? request.batchSize()
                        : properties.getBatchSize();

        int upcomingDays =
                request.upcomingDays() != null
                        ? request.upcomingDays()
                        : properties.getUpcomingDays();

        LocalDateTime startedAt =
                LocalDateTime.now();

        FeatureEngineeringService
                .FeatureEngineeringResult result =
                featureEngineeringService
                        .generateFeatures(
                                request.shouldSettleUpcoming(),
                                request.shouldGenerateTraining(),
                                request.shouldGenerateUpcoming(),
                                batchSize,
                                upcomingDays
                        );

        String status =
                result.failed() == 0
                        ? "COMPLETED"
                        : result.settled()
                                        + result.trainingGenerated()
                                        + result.upcomingGenerated()
                                > 0
                                ? "PARTIAL_SUCCESS"
                                : "FAILED";

        return new FeatureEngineeringRunResponse(
                status,
                result.settled(),
                result.trainingGenerated(),
                result.upcomingGenerated(),
                result.failed(),
                batchSize,
                upcomingDays,
                startedAt,
                LocalDateTime.now(),
                result.errors()
        );
    }
}