package com.footballai.engineering.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record UpcomingFeatureResponse(

        Long fixtureId,

        Long leagueId,

        String leagueName,

        Long homeTeamId,

        String homeTeamName,

        Long awayTeamId,

        String awayTeamName,

        LocalDateTime kickoff,

        Integer featuresCount,

        Map<String, BigDecimal> features

) {
}