package com.footballai.engineering.service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.footballai.engineering.service.dto.UpcomingFixtureFeaturesResponse;
import com.footballai.engineering.service.entity.Fixture;
import com.footballai.engineering.service.entity.League;
import com.footballai.engineering.service.entity.PredictionFeature;
import com.footballai.engineering.service.entity.Team;
import com.footballai.engineering.service.exception.FeatureReferenceException;
import com.footballai.engineering.service.mapper.FeatureVectorMapper;
import com.footballai.engineering.service.repository.FixtureRepository;
import com.footballai.engineering.service.repository.LeagueRepository;
import com.footballai.engineering.service.repository.PredictionFeatureRepository;
import com.footballai.engineering.service.repository.TeamRepository;
import com.footballai.engineering.service.utils.FeatureRetrievalMode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpcomingFixtureFeaturesService {

    private final FixtureRepository fixtureRepository;

    private final PredictionFeatureRepository
            predictionFeatureRepository;

    private final TeamRepository teamRepository;

    private final LeagueRepository leagueRepository;

    private final FeatureVectorMapper featureVectorMapper;

    @Transactional(readOnly = true)
    public List<UpcomingFixtureFeaturesResponse> findFixtures(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String featureVersion,
            FeatureRetrievalMode mode
    ) {

        validateRequest(
                fromDate,
                toDate,
                featureVersion,
                mode
        );

        List<Fixture> fixtures;

        if (mode == FeatureRetrievalMode.HISTORICAL) {

            fixtures =
                    fixtureRepository
                            .findHistoricalFixturesWithFeatures(
                                    fromDate,
                                    toDate,
                                    featureVersion
                            );

        } else {

            fixtures =
                    fixtureRepository
                            .findLiveFixturesWithFeatures(
                                    fromDate,
                                    toDate,
                                    featureVersion
                            );
        }

        if (fixtures.isEmpty()) {
            return Collections.emptyList();
        }

        // Da qui in poi rimane invariato il codice attuale.

        Set<Long> fixtureIds =
                fixtures.stream()
                        .map(Fixture::getId)
                        .collect(
                                Collectors.toCollection(
                                        LinkedHashSet::new
                                )
                        );

        List<PredictionFeature> featureEntities =
                predictionFeatureRepository
                        .findByFixtureIdInAndFeatureVersion(
                                fixtureIds,
                                featureVersion
                        );

        Map<Long, PredictionFeature> featuresByFixtureId =
                featureEntities.stream()
                        .collect(
                                Collectors.toMap(
                                        PredictionFeature::getFixtureId,
                                        Function.identity()
                                )
                        );

        Set<Long> teamIds =
                new LinkedHashSet<>();

        Set<Long> leagueIds =
                new LinkedHashSet<>();

        for (Fixture fixture : fixtures) {

            teamIds.add(
                    fixture.getHomeTeamId()
            );

            teamIds.add(
                    fixture.getAwayTeamId()
            );

            leagueIds.add(
                    fixture.getLeagueId()
            );
        }

        Map<Long, Team> teamsById =
                teamRepository
                        .findAllById(teamIds)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Team::getId,
                                        Function.identity()
                                )
                        );

        Map<Long, League> leaguesById =
                leagueRepository
                        .findAllById(leagueIds)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        League::getId,
                                        Function.identity()
                                )
                        );

        return fixtures.stream()
                .map(
                        fixture -> toResponse(
                                fixture,
                                featuresByFixtureId,
                                teamsById,
                                leaguesById
                        )
                )
                .toList();
    }

    private UpcomingFixtureFeaturesResponse toResponse(
            Fixture fixture,
            Map<Long, PredictionFeature>
                    featuresByFixtureId,
            Map<Long, Team> teamsById,
            Map<Long, League> leaguesById
    ) {

        PredictionFeature predictionFeature =
                featuresByFixtureId.get(
                        fixture.getId()
                );

        if (predictionFeature == null) {

            throw new FeatureReferenceException(
                    "PredictionFeature not found "
                            + "for fixture "
                            + fixture.getId()
                );
        }

        Team homeTeam =
                teamsById.get(
                        fixture.getHomeTeamId()
                );

        if (homeTeam == null) {

            throw new FeatureReferenceException(
                    "Home team "
                            + fixture.getHomeTeamId()
                            + " not found for fixture "
                            + fixture.getId()
            );
        }

        Team awayTeam =
                teamsById.get(
                        fixture.getAwayTeamId()
                );

        if (awayTeam == null) {

            throw new FeatureReferenceException(
                    "Away team "
                            + fixture.getAwayTeamId()
                            + " not found for fixture "
                            + fixture.getId()
            );
        }

        League league =
                leaguesById.get(
                        fixture.getLeagueId()
                );

        if (league == null) {

            throw new FeatureReferenceException(
                    "League "
                            + fixture.getLeagueId()
                            + " not found for fixture "
                            + fixture.getId()
            );
        }

        List<BigDecimal> features =
                featureVectorMapper.toVector(
                        predictionFeature
                );

        return UpcomingFixtureFeaturesResponse
                .builder()
                .fixtureId(
                        fixture.getId()
                )
                .leagueId(
                        league.getId()
                )
                .leagueName(
                        league.getName()
                )
                .leagueCountry(
                        league.getCountry()
                )
                .season(
                        fixture.getSeason()
                )
                .kickoff(
                        fixture.getDate()
                )
                .status(
                        fixture.getStatus()
                )
                .homeTeamId(
                        homeTeam.getId()
                )
                .homeTeamName(
                        homeTeam.getName()
                )
                .awayTeamId(
                        awayTeam.getId()
                )
                .awayTeamName(
                        awayTeam.getName()
                )
                .featureVersion(
                        predictionFeature
                                .getFeatureVersion()
                )
                .featuresCount(
                        features.size()
                )
                .features(
                        features
                )
                .build();
    }

    private void validateRequest(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            String featureVersion,
            FeatureRetrievalMode mode
    ) {

        if (fromDate == null) {
            throw new IllegalArgumentException(
                    "from must not be null"
            );
        }

        if (toDate == null) {
            throw new IllegalArgumentException(
                    "to must not be null"
            );
        }

        if (!toDate.isAfter(fromDate)) {
            throw new IllegalArgumentException(
                    "to must be after from"
            );
        }

        if (featureVersion == null
                || featureVersion.isBlank()) {

            throw new IllegalArgumentException(
                    "featureVersion must not be blank"
            );
        }

        if (mode == null) {
            throw new IllegalArgumentException(
                    "mode must not be null"
            );
        }
    }
}