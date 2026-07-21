package com.footballai.engineering.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UpcomingFixtureFeaturesResponse {

    private Long fixtureId;

    private Long leagueId;

    private String leagueName;

    private String leagueCountry;

    private Integer season;

    private LocalDateTime kickoff;

    private String status;

    private Long homeTeamId;

    private String homeTeamName;

    private Long awayTeamId;

    private String awayTeamName;

    private String featureVersion;

    private Integer featuresCount;

    private List<BigDecimal> features;
}