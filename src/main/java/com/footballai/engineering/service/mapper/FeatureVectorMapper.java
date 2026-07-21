package com.footballai.engineering.service.mapper;

import com.footballai.engineering.service.entity.PredictionFeature;
import com.footballai.engineering.service.exception.InvalidFeatureVectorException;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class FeatureVectorMapper {

    public static final int EXPECTED_FEATURES_COUNT = 62;

    public List<BigDecimal> toVector(
            PredictionFeature feature
    ) {

        List<BigDecimal> values =
                new ArrayList<>(
                        EXPECTED_FEATURES_COUNT
                );

        values.add(
                feature.getHomeTeamForm5()
        );

        values.add(
                feature.getAwayTeamForm5()
        );

        values.add(
                feature.getHomeTeamForm10()
        );

        values.add(
                feature.getAwayTeamForm10()
        );

        values.add(
                feature.getHomeAvgGoalsScored()
        );

        values.add(
                feature.getAwayAvgGoalsScored()
        );

        values.add(
                feature.getHomeAvgGoalsConceded()
        );

        values.add(
                feature.getAwayAvgGoalsConceded()
        );

        values.add(
                feature.getHomeWinRate()
        );

        values.add(
                feature.getAwayWinRate()
        );

        values.add(
                feature.getHomeCleanSheetRate()
        );

        values.add(
                feature.getAwayCleanSheetRate()
        );

        values.add(
                toBigDecimal(
                        feature.getHomeTablePosition()
                )
        );

        values.add(
                toBigDecimal(
                        feature.getAwayTablePosition()
                )
        );

        values.add(
                toBigDecimal(
                        feature.getHomeRestDays()
                )
        );

        values.add(
                toBigDecimal(
                        feature.getAwayRestDays()
                )
        );

        values.add(
                feature.getHomeAvgShots()
        );

        values.add(
                feature.getAwayAvgShots()
        );

        values.add(
                feature.getHomeAvgShotsOnGoal()
        );

        values.add(
                feature.getAwayAvgShotsOnGoal()
        );

        values.add(
                feature.getHomeAvgPossession()
        );

        values.add(
                feature.getAwayAvgPossession()
        );

        values.add(
                feature.getHomeAvgPassAccuracy()
        );

        values.add(
                feature.getAwayAvgPassAccuracy()
        );

        values.add(
                feature.getHomeAvgCorners()
        );

        values.add(
                feature.getAwayAvgCorners()
        );

        values.add(
                feature.getHomeAvgShotsInsideBox()
        );

        values.add(
                feature.getAwayAvgShotsInsideBox()
        );

        values.add(
                feature.getHomeAvgXg()
        );

        values.add(
                feature.getAwayAvgXg()
        );

        values.add(
                feature.getHomeAvgXga()
        );

        values.add(
                feature.getAwayAvgXga()
        );

        values.add(
                feature.getHomeHomeAvgGoalsScored()
        );

        values.add(
                feature.getHomeHomeAvgGoalsConceded()
        );

        values.add(
                feature.getHomeHomeAvgShots()
        );

        values.add(
                feature.getHomeHomeAvgXg()
        );

        values.add(
                feature.getHomeHomeAvgXga()
        );

        values.add(
                feature.getAwayAwayAvgGoalsScored()
        );

        values.add(
                feature.getAwayAwayAvgGoalsConceded()
        );

        values.add(
                feature.getAwayAwayAvgShots()
        );

        values.add(
                feature.getAwayAwayAvgXg()
        );

        values.add(
                feature.getAwayAwayAvgXga()
        );

        values.add(
                feature.getHomeBttsRate5()
        );

        values.add(
                feature.getAwayBttsRate5()
        );

        values.add(
                feature.getHomeBttsRate10()
        );

        values.add(
                feature.getAwayBttsRate10()
        );

        values.add(
                feature.getHomeOver25Rate5()
        );

        values.add(
                feature.getAwayOver25Rate5()
        );

        values.add(
                feature.getHomeOver25Rate10()
        );

        values.add(
                feature.getAwayOver25Rate10()
        );

        values.add(
                feature.getCombinedAvgXg()
        );

        values.add(
                feature.getExpectedMatchGoals()
        );

        values.add(
                feature.getFormDifference()
        );

        values.add(
                feature.getXgDifference()
        );

        values.add(
                feature.getXgaDifference()
        );

        values.add(
                feature.getGoalsScoredDifference()
        );

        values.add(
                feature.getGoalsConcededDifference()
        );

        values.add(
                feature.getShotsDifference()
        );

        values.add(
                feature.getShotsOnGoalDifference()
        );

        values.add(
                feature.getPossessionDifference()
        );

        values.add(
                feature.getCornersDifference()
        );

        values.add(
                feature.getPassAccuracyDifference()
        );

        validateVector(
                feature.getFixtureId(),
                values
        );

        return List.copyOf(
                values
        );
    }

    private void validateVector(
            Long fixtureId,
            List<BigDecimal> values
    ) {

        if (values.size()
                != EXPECTED_FEATURES_COUNT) {

            throw new InvalidFeatureVectorException(
                    "Fixture "
                            + fixtureId
                            + " contains "
                            + values.size()
                            + " features; expected "
                            + EXPECTED_FEATURES_COUNT
            );
        }

        for (int index = 0;
             index < values.size();
             index++) {

            if (values.get(index) == null) {

                throw new InvalidFeatureVectorException(
                        "Fixture "
                                + fixtureId
                                + " contains a null value "
                                + "at feature index "
                                + index
                );
            }
        }
    }

    private BigDecimal toBigDecimal(
            Integer value
    ) {

        if (value == null) {
            return null;
        }

        return BigDecimal.valueOf(
                value.longValue()
        );
    }
}