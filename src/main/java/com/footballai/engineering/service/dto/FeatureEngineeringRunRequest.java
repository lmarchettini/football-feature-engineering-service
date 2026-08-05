package com.footballai.engineering.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record FeatureEngineeringRunRequest(

        Boolean settleUpcoming,

        Boolean generateTraining,

        Boolean generateUpcoming,

        @Min(1)
        @Max(5000)
        Integer batchSize,

        @Min(1)
        @Max(30)
        Integer upcomingDays

) {

    public boolean shouldSettleUpcoming() {
        return Boolean.TRUE.equals(
                settleUpcoming
        );
    }

    public boolean shouldGenerateTraining() {
        return Boolean.TRUE.equals(
                generateTraining
        );
    }

    public boolean shouldGenerateUpcoming() {
        return Boolean.TRUE.equals(
                generateUpcoming
        );
    }
}