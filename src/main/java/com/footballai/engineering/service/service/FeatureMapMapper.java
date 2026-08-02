package com.footballai.engineering.service.service;



import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.footballai.engineering.service.entity.PredictionFeature;



@Component
public class FeatureMapMapper {

    public Map<String, BigDecimal> toMap(
            PredictionFeature feature
    ) {
        Map<String, BigDecimal> result =
                new LinkedHashMap<>();

        /*
         * Feature generali
         */

        put(result,
                "home_team_form_5",
                feature.getHomeTeamForm5());

        put(result,
                "away_team_form_5",
                feature.getAwayTeamForm5());

        put(result,
                "home_team_form_10",
                feature.getHomeTeamForm10());

        put(result,
                "away_team_form_10",
                feature.getAwayTeamForm10());

        put(result,
                "home_avg_goals_scored",
                feature.getHomeAvgGoalsScored());

        put(result,
                "away_avg_goals_scored",
                feature.getAwayAvgGoalsScored());

        put(result,
                "home_avg_goals_conceded",
                feature.getHomeAvgGoalsConceded());

        put(result,
                "away_avg_goals_conceded",
                feature.getAwayAvgGoalsConceded());

        put(result,
                "home_win_rate",
                feature.getHomeWinRate());

        put(result,
                "away_win_rate",
                feature.getAwayWinRate());

        put(result,
                "home_clean_sheet_rate",
                feature.getHomeCleanSheetRate());

        put(result,
                "away_clean_sheet_rate",
                feature.getAwayCleanSheetRate());

        put(result,
                "home_table_position",
                feature.getHomeTablePosition());

        put(result,
                "away_table_position",
                feature.getAwayTablePosition());

        put(result,
                "home_rest_days",
                feature.getHomeRestDays());

        put(result,
                "away_rest_days",
                feature.getAwayRestDays());

        /*
         * Statistiche
         */

        put(result,
                "home_avg_shots",
                feature.getHomeAvgShots());

        put(result,
                "away_avg_shots",
                feature.getAwayAvgShots());

        put(result,
                "home_avg_shots_on_goal",
                feature.getHomeAvgShotsOnGoal());

        put(result,
                "away_avg_shots_on_goal",
                feature.getAwayAvgShotsOnGoal());

        put(result,
                "home_avg_possession",
                feature.getHomeAvgPossession());

        put(result,
                "away_avg_possession",
                feature.getAwayAvgPossession());

        put(result,
                "home_avg_pass_accuracy",
                feature.getHomeAvgPassAccuracy());

        put(result,
                "away_avg_pass_accuracy",
                feature.getAwayAvgPassAccuracy());

        put(result,
                "home_avg_corners",
                feature.getHomeAvgCorners());

        put(result,
                "away_avg_corners",
                feature.getAwayAvgCorners());

        put(result,
                "home_avg_shots_inside_box",
                feature.getHomeAvgShotsInsideBox());

        put(result,
                "away_avg_shots_inside_box",
                feature.getAwayAvgShotsInsideBox());

        /*
         * Expected goals
         */

        put(result,
                "home_avg_xg",
                feature.getHomeAvgXg());

        put(result,
                "away_avg_xg",
                feature.getAwayAvgXg());

        put(result,
                "home_avg_xga",
                feature.getHomeAvgXga());

        put(result,
                "away_avg_xga",
                feature.getAwayAvgXga());

        /*
         * Split casa/trasferta
         */

        put(result,
                "home_home_avg_goals_scored",
                feature.getHomeHomeAvgGoalsScored());

        put(result,
                "home_home_avg_goals_conceded",
                feature.getHomeHomeAvgGoalsConceded());

        put(result,
                "home_home_avg_shots",
                feature.getHomeHomeAvgShots());

        put(result,
                "home_home_avg_xg",
                feature.getHomeHomeAvgXg());

        put(result,
                "home_home_avg_xga",
                feature.getHomeHomeAvgXga());

        put(result,
                "away_away_avg_goals_scored",
                feature.getAwayAwayAvgGoalsScored());

        put(result,
                "away_away_avg_goals_conceded",
                feature.getAwayAwayAvgGoalsConceded());

        put(result,
                "away_away_avg_shots",
                feature.getAwayAwayAvgShots());

        put(result,
                "away_away_avg_xg",
                feature.getAwayAwayAvgXg());

        put(result,
                "away_away_avg_xga",
                feature.getAwayAwayAvgXga());

        /*
         * BTTS
         */

        put(result,
                "home_btts_rate_5",
                feature.getHomeBttsRate5());

        put(result,
                "away_btts_rate_5",
                feature.getAwayBttsRate5());

        put(result,
                "home_btts_rate_10",
                feature.getHomeBttsRate10());

        put(result,
                "away_btts_rate_10",
                feature.getAwayBttsRate10());

        /*
         * Over 2.5
         */

        put(result,
                "home_over25_rate_5",
                feature.getHomeOver25Rate5());

        put(result,
                "away_over25_rate_5",
                feature.getAwayOver25Rate5());

        put(result,
                "home_over25_rate_10",
                feature.getHomeOver25Rate10());

        put(result,
                "away_over25_rate_10",
                feature.getAwayOver25Rate10());

        /*
         * Feature combinate e differenze
         */

        put(result,
                "combined_avg_xg",
                feature.getCombinedAvgXg());

        put(result,
                "expected_match_goals",
                feature.getExpectedMatchGoals());

        put(result,
                "form_difference",
                feature.getFormDifference());

        put(result,
                "xg_difference",
                feature.getXgDifference());

        put(result,
                "xga_difference",
                feature.getXgaDifference());

        put(result,
                "goals_scored_difference",
                feature.getGoalsScoredDifference());

        put(result,
                "goals_conceded_difference",
                feature.getGoalsConcededDifference());

        put(result,
                "shots_difference",
                feature.getShotsDifference());

        put(result,
                "shots_on_goal_difference",
                feature.getShotsOnGoalDifference());

        put(result,
                "possession_difference",
                feature.getPossessionDifference());

        put(result,
                "corners_difference",
                feature.getCornersDifference());

        put(result,
                "pass_accuracy_difference",
                feature.getPassAccuracyDifference());

        /*
         * Nuove 14 feature
         */

        put(result,
                "home_scored_rate_5",
                feature.getHomeScoredRate5());

        put(result,
                "away_scored_rate_5",
                feature.getAwayScoredRate5());

        put(result,
                "home_scored_rate_10",
                feature.getHomeScoredRate10());

        put(result,
                "away_scored_rate_10",
                feature.getAwayScoredRate10());

        put(result,
                "home_conceded_rate_5",
                feature.getHomeConcededRate5());

        put(result,
                "away_conceded_rate_5",
                feature.getAwayConcededRate5());

        put(result,
                "home_conceded_rate_10",
                feature.getHomeConcededRate10());

        put(result,
                "away_conceded_rate_10",
                feature.getAwayConcededRate10());

        put(result,
                "home_avg_total_goals_5",
                feature.getHomeAvgTotalGoals5());

        put(result,
                "away_avg_total_goals_5",
                feature.getAwayAvgTotalGoals5());

        put(result,
                "home_avg_total_goals_10",
                feature.getHomeAvgTotalGoals10());

        put(result,
                "away_avg_total_goals_10",
                feature.getAwayAvgTotalGoals10());

        put(result,
                "expected_home_goals",
                feature.getExpectedHomeGoals());

        put(result,
                "expected_away_goals",
                feature.getExpectedAwayGoals());
        
        
        /*
         * Gol stimati con fallback
         */

        put(result,
                "estimated_home_goals",
                feature.getEstimatedHomeGoals());

        put(result,
                "estimated_away_goals",
                feature.getEstimatedAwayGoals());
        
        /*
         * BTTS interaction features
         */

        put(result,
                "min_expected_goals",
                feature.getMinExpectedGoals());

        put(result,
                "max_expected_goals",
                feature.getMaxExpectedGoals());

        put(result,
                "expected_goals_gap",
                feature.getExpectedGoalsGap());

        put(result,
                "expected_goals_product",
                feature.getExpectedGoalsProduct());

        put(result,
                "min_scored_rate_5",
                feature.getMinScoredRate5());

        put(result,
                "min_scored_rate_10",
                feature.getMinScoredRate10());

        put(result,
                "scored_rate_product_10",
                feature.getScoredRateProduct10());

        put(result,
                "min_conceded_rate_5",
                feature.getMinConcededRate5());

        put(result,
                "min_conceded_rate_10",
                feature.getMinConcededRate10());

        put(result,
                "conceded_rate_product_10",
                feature.getConcededRateProduct10());

        put(result,
                "home_attack_vs_away_defence",
                feature.getHomeAttackVsAwayDefence());

        put(result,
                "away_attack_vs_home_defence",
                feature.getAwayAttackVsHomeDefence());

        return Map.copyOf(result);
    }

    private void put(
            Map<String, BigDecimal> target,
            String name,
            Number value
    ) {
        if (value == null) {
            throw new IllegalStateException(
                    "Feature "
                            + name
                            + " has null value"
            );
        }

        target.put(
                name,
                new BigDecimal(
                        value.toString()
                )
        );
    }
}