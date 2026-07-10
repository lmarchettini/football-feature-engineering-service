package com.footballai.engineering.service.service;

import java.math.BigDecimal;

public record TeamStats(
        BigDecimal form,
        BigDecimal avgGoalsScored,
        BigDecimal avgGoalsConceded,
        BigDecimal winRate,
        BigDecimal cleanSheetRate,
        Integer restDays,
        BigDecimal avgShots,
        BigDecimal avgShotsOnGoal,
        BigDecimal avgPossession,
        BigDecimal avgPassAccuracy,
        BigDecimal avgCorners,
        BigDecimal avgShotsInsideBox,
        BigDecimal avgXg,
        BigDecimal avgXga
) {

}