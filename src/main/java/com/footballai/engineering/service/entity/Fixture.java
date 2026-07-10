package com.footballai.engineering.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fixtures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fixture {

    @Id
    private Long id;

    private Long leagueId;

    private Integer season;

    private LocalDateTime date;

    private String status;

    private Long homeTeamId;

    private Long awayTeamId;

    private Integer homeGoals;

    private Integer awayGoals;

    private Long venueId;

    private String round;

    private String referee;
}