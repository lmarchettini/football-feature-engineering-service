package com.footballai.engineering.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fixture_statistics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixtureStatistic {

    @Id
    private Long id;

    @Column(name="fixture_id")
    private Long fixtureId;

    @Column(name="team_id")
    private Long teamId;

    @Column(name="shots_total")
    private Integer shotsTotal;

    @Column(name="shots_on_goal")
    private Integer shotsOnGoal;

    @Column(name="expected_goals")
    private BigDecimal expectedGoals;
    
    @Column(name = "possession")
    private BigDecimal possession;

    @Column(name = "corners")
    private Integer corners;

    @Column(name = "pass_accuracy")
    private BigDecimal passAccuracy;

    @Column(name = "shots_inside_box")
    private Integer shotsInsideBox;
}