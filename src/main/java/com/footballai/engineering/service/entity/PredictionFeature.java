package com.footballai.engineering.service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prediction_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionFeature {

    @Id
    @Column(name = "fixture_id")
    private Long fixtureId;

    @Column(name = "home_team_form_5")
    private BigDecimal homeTeamForm5;

    @Column(name = "away_team_form_5")
    private BigDecimal awayTeamForm5;

    @Column(name = "home_avg_goals_scored")
    private BigDecimal homeAvgGoalsScored;

    @Column(name = "away_avg_goals_scored")
    private BigDecimal awayAvgGoalsScored;

    @Column(name = "home_avg_goals_conceded")
    private BigDecimal homeAvgGoalsConceded;

    @Column(name = "away_avg_goals_conceded")
    private BigDecimal awayAvgGoalsConceded;

    @Column(name = "home_avg_shots")
    private BigDecimal homeAvgShots;

    @Column(name = "away_avg_shots")
    private BigDecimal awayAvgShots;

    @Column(name = "home_win_rate")
    private BigDecimal homeWinRate;

    @Column(name = "away_win_rate")
    private BigDecimal awayWinRate;

    @Column(name = "target_goal")
    private Boolean targetGoal;

    @Column(name = "home_clean_sheet_rate")
    private BigDecimal homeCleanSheetRate;

    @Column(name = "away_clean_sheet_rate")
    private BigDecimal awayCleanSheetRate;

    @Column(name = "h2h_home_wins")
    private Integer h2hHomeWins;

    @Column(name = "h2h_away_wins")
    private Integer h2hAwayWins;

    @Column(name = "home_rest_days")
    private Integer homeRestDays;

    @Column(name = "away_rest_days")
    private Integer awayRestDays;

    @Column(name = "home_table_position")
    private Integer homeTablePosition;

    @Column(name = "away_table_position")
    private Integer awayTablePosition;

    @Column(name = "odds_home")
    private BigDecimal oddsHome;

    @Column(name = "odds_draw")
    private BigDecimal oddsDraw;

    @Column(name = "odds_away")
    private BigDecimal oddsAway;

    @Column(name = "target_over_25")
    private Boolean targetOver25;

    @Column(name = "target_over_15")
    private Boolean targetOver15;

    @Column(name = "target_under_45")
    private Boolean targetUnder45;

    @Column(name = "target_double_chance_1x")
    private Boolean targetDoubleChance1x;

    @Column(name = "target_double_chance_x2")
    private Boolean targetDoubleChanceX2;

    @Column(name = "target_double_chance_12")
    private Boolean targetDoubleChance12;

    @Column(name = "target_btts")
    private Boolean targetBtts;

    @Column(name = "target_home_win")
    private Boolean targetHomeWin;

    @Column(name = "home_avg_xg")
    private BigDecimal homeAvgXg;

    @Column(name = "away_avg_xg")
    private BigDecimal awayAvgXg;

    @Column(name = "home_avg_xga")
    private BigDecimal homeAvgXga;

    @Column(name = "away_avg_xga")
    private BigDecimal awayAvgXga;

    @Column(name = "is_trainable")
    private Boolean isTrainable;

    @Column(name = "home_avg_shots_on_goal")
    private BigDecimal homeAvgShotsOnGoal;

    @Column(name = "away_avg_shots_on_goal")
    private BigDecimal awayAvgShotsOnGoal;

    @Column(name = "home_avg_possession")
    private BigDecimal homeAvgPossession;

    @Column(name = "away_avg_possession")
    private BigDecimal awayAvgPossession;

    @Column(name = "home_avg_pass_accuracy")
    private BigDecimal homeAvgPassAccuracy;

    @Column(name = "away_avg_pass_accuracy")
    private BigDecimal awayAvgPassAccuracy;

    @Column(name = "home_team_form_10")
    private BigDecimal homeTeamForm10;

    @Column(name = "away_team_form_10")
    private BigDecimal awayTeamForm10;

    @Column(name = "home_avg_corners")
    private BigDecimal homeAvgCorners;

    @Column(name = "away_avg_corners")
    private BigDecimal awayAvgCorners;

    @Column(name = "home_avg_shots_inside_box")
    private BigDecimal homeAvgShotsInsideBox;

    @Column(name = "away_avg_shots_inside_box")
    private BigDecimal awayAvgShotsInsideBox;

    @Column(name = "home_home_avg_goals_scored")
    private BigDecimal homeHomeAvgGoalsScored;

    @Column(name = "home_home_avg_goals_conceded")
    private BigDecimal homeHomeAvgGoalsConceded;

    @Column(name = "home_home_avg_shots")
    private BigDecimal homeHomeAvgShots;

    @Column(name = "home_home_avg_xg")
    private BigDecimal homeHomeAvgXg;

    @Column(name = "home_home_avg_xga")
    private BigDecimal homeHomeAvgXga;

    @Column(name = "away_away_avg_goals_scored")
    private BigDecimal awayAwayAvgGoalsScored;

    @Column(name = "away_away_avg_goals_conceded")
    private BigDecimal awayAwayAvgGoalsConceded;

    @Column(name = "away_away_avg_shots")
    private BigDecimal awayAwayAvgShots;

    @Column(name = "away_away_avg_xg")
    private BigDecimal awayAwayAvgXg;

    @Column(name = "away_away_avg_xga")
    private BigDecimal awayAwayAvgXga;

    @Column(name = "home_btts_rate_5")
    private BigDecimal homeBttsRate5;

    @Column(name = "away_btts_rate_5")
    private BigDecimal awayBttsRate5;

    @Column(name = "home_btts_rate_10")
    private BigDecimal homeBttsRate10;

    @Column(name = "away_btts_rate_10")
    private BigDecimal awayBttsRate10;

    @Column(name = "home_over25_rate_5")
    private BigDecimal homeOver25Rate5;

    @Column(name = "away_over25_rate_5")
    private BigDecimal awayOver25Rate5;

    @Column(name = "home_over25_rate_10")
    private BigDecimal homeOver25Rate10;

    @Column(name = "away_over25_rate_10")
    private BigDecimal awayOver25Rate10;

    @Column(name = "combined_avg_xg")
    private BigDecimal combinedAvgXg;

    @Column(name = "expected_match_goals")
    private BigDecimal expectedMatchGoals;

    // ===== GOAL MARKET FEATURES: BTTS / OVER 2.5 =====

    /**
     * Percentuale delle ultime 5 partite della squadra di casa
     * in cui la squadra ha segnato almeno un gol.
     */
    @Column(name = "home_scored_rate_5")
    private BigDecimal homeScoredRate5;

    /**
     * Percentuale delle ultime 5 partite della squadra ospite
     * in cui la squadra ha segnato almeno un gol.
     */
    @Column(name = "away_scored_rate_5")
    private BigDecimal awayScoredRate5;

    /**
     * Percentuale delle ultime 10 partite della squadra di casa
     * in cui la squadra ha segnato almeno un gol.
     */
    @Column(name = "home_scored_rate_10")
    private BigDecimal homeScoredRate10;

    /**
     * Percentuale delle ultime 10 partite della squadra ospite
     * in cui la squadra ha segnato almeno un gol.
     */
    @Column(name = "away_scored_rate_10")
    private BigDecimal awayScoredRate10;

    /**
     * Percentuale delle ultime 5 partite della squadra di casa
     * in cui la squadra ha subito almeno un gol.
     */
    @Column(name = "home_conceded_rate_5")
    private BigDecimal homeConcededRate5;

    /**
     * Percentuale delle ultime 5 partite della squadra ospite
     * in cui la squadra ha subito almeno un gol.
     */
    @Column(name = "away_conceded_rate_5")
    private BigDecimal awayConcededRate5;

    /**
     * Percentuale delle ultime 10 partite della squadra di casa
     * in cui la squadra ha subito almeno un gol.
     */
    @Column(name = "home_conceded_rate_10")
    private BigDecimal homeConcededRate10;

    /**
     * Percentuale delle ultime 10 partite della squadra ospite
     * in cui la squadra ha subito almeno un gol.
     */
    @Column(name = "away_conceded_rate_10")
    private BigDecimal awayConcededRate10;

    /**
     * Media dei gol totali delle ultime 5 partite della squadra di casa:
     * gol segnati + gol subiti.
     */
    @Column(name = "home_avg_total_goals_5")
    private BigDecimal homeAvgTotalGoals5;

    /**
     * Media dei gol totali delle ultime 5 partite della squadra ospite:
     * gol segnati + gol subiti.
     */
    @Column(name = "away_avg_total_goals_5")
    private BigDecimal awayAvgTotalGoals5;

    /**
     * Media dei gol totali delle ultime 10 partite della squadra di casa:
     * gol segnati + gol subiti.
     */
    @Column(name = "home_avg_total_goals_10")
    private BigDecimal homeAvgTotalGoals10;

    /**
     * Media dei gol totali delle ultime 10 partite della squadra ospite:
     * gol segnati + gol subiti.
     */
    @Column(name = "away_avg_total_goals_10")
    private BigDecimal awayAvgTotalGoals10;

    /**
     * Numero atteso di gol della squadra di casa.
     */
    @Column(name = "expected_home_goals")
    private BigDecimal expectedHomeGoals;

    /**
     * Numero atteso di gol della squadra ospite.
     */
    @Column(name = "expected_away_goals")
    private BigDecimal expectedAwayGoals;

    // ===== V5: DIFFERENCE FEATURES =====

    @Column(name = "form_difference")
    private BigDecimal formDifference;

    @Column(name = "xg_difference")
    private BigDecimal xgDifference;

    @Column(name = "xga_difference")
    private BigDecimal xgaDifference;

    @Column(name = "goals_scored_difference")
    private BigDecimal goalsScoredDifference;

    @Column(name = "goals_conceded_difference")
    private BigDecimal goalsConcededDifference;

    @Column(name = "shots_difference")
    private BigDecimal shotsDifference;

    @Column(name = "shots_on_goal_difference")
    private BigDecimal shotsOnGoalDifference;

    @Column(name = "possession_difference")
    private BigDecimal possessionDifference;

    @Column(name = "corners_difference")
    private BigDecimal cornersDifference;

    @Column(name = "pass_accuracy_difference")
    private BigDecimal passAccuracyDifference;
}