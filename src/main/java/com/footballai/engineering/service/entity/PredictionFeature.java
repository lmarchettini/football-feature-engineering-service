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
    
    @Column(name = "feature_version")
    private String featureVersion;
    
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
}