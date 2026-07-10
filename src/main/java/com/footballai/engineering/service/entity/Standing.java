package com.footballai.engineering.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "standings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Standing {

    @Id
    private Long id;

    private Long leagueId;

    private Integer season;

    private Long teamId;

    @Column(name = "position")
    private Integer position;

    private Integer points;

    private Integer goalDiff;

    private String form;
}