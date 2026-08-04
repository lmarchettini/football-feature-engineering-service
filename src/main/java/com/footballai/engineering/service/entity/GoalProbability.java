package com.footballai.engineering.service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_probabilities")
@IdClass(GoalProbabilityId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalProbability {

    @Id
    @Column(name = "fixture_id")
    private Long fixtureId;

    @Id
    @Column(name = "engine")
    private String engine;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "expected_home_goals")
    private BigDecimal expectedHomeGoals;

    @Column(name = "expected_away_goals")
    private BigDecimal expectedAwayGoals;

    @Column(name = "expected_total_goals")
    private BigDecimal expectedTotalGoals;

    @Column(name = "home_scored_probability")
    private BigDecimal homeScoredProbability;

    @Column(name = "away_scored_probability")
    private BigDecimal awayScoredProbability;
}