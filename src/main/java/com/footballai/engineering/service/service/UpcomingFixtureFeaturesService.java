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

import com.footballai.engineering.service.dto.UpcomingFeatureResponse;
import com.footballai.engineering.service.entity.Fixture;
import com.footballai.engineering.service.entity.GoalProbability;
import com.footballai.engineering.service.entity.League;
import com.footballai.engineering.service.entity.PredictionFeature;
import com.footballai.engineering.service.entity.Team;
import com.footballai.engineering.service.exception.FeatureReferenceException;
import com.footballai.engineering.service.repository.FixtureRepository;
import com.footballai.engineering.service.repository.GoalProbabilityRepository;
import com.footballai.engineering.service.repository.LeagueRepository;
import com.footballai.engineering.service.repository.PredictionFeatureRepository;
import com.footballai.engineering.service.repository.TeamRepository;
import com.footballai.engineering.service.utils.FeatureRetrievalMode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpcomingFixtureFeaturesService {

    private final FixtureRepository fixtureRepository;

    private final PredictionFeatureRepository
            predictionFeatureRepository;

    private final TeamRepository teamRepository;

    private final LeagueRepository leagueRepository;

    private final FeatureMapMapper featureMapMapper;
    
    private final GoalProbabilityRepository
    goalProbabilityRepository;

    @Transactional(readOnly = true)
    public List<UpcomingFeatureResponse> findFixtures(
            LocalDateTime fromDate,
            LocalDateTime toDate,
            FeatureRetrievalMode mode
    ) {
        validateRequest(
                fromDate,
                toDate,
                mode
        );

        List<Fixture> fixtures;

        if (mode == FeatureRetrievalMode.HISTORICAL) {
            fixtures =
                    fixtureRepository
                            .findHistoricalFixturesWithFeatures(
                                    fromDate,
                                    toDate
                            );
        } else {
            fixtures =
                    fixtureRepository
                            .findLiveFixturesWithFeatures(
                                    fromDate,
                                    toDate
                            );
        }

        if (fixtures.isEmpty()) {
            return Collections.emptyList();
        }

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
                        .findByFixtureIdIn(
                                fixtureIds
                        );

        Map<Long, PredictionFeature> featuresByFixtureId =
                featureEntities.stream()
                        .collect(
                                Collectors.toMap(
                                        PredictionFeature::getFixtureId,
                                        Function.identity()
                                )
                        );
        
        List<GoalProbability> goalProbabilities =
                goalProbabilityRepository
                        .findByFixtureIdInAndEngine(
                                fixtureIds,
                                "DIXON_COLES"
                        );

        Map<Long, GoalProbability>
                goalProbabilitiesByFixtureId =
                goalProbabilities.stream()
                        .collect(
                                Collectors.toMap(
                                        GoalProbability::getFixtureId,
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
                .map(fixture -> {
                    try {
                        return toResponse(
                                fixture,
                                featuresByFixtureId,
                                goalProbabilitiesByFixtureId,
                                teamsById,
                                leaguesById
                        );
                    } catch (Exception exception) {
                        log.error(
                                "Failed to build feature response: "
                                        + "fixtureId={}, status={}, date={}, "
                                        + "leagueId={}, homeTeamId={}, awayTeamId={}",
                                fixture.getId(),
                                fixture.getStatus(),
                                fixture.getDate(),
                                fixture.getLeagueId(),
                                fixture.getHomeTeamId(),
                                fixture.getAwayTeamId(),
                                exception
                        );

                        throw exception;
                    }
                })
                .toList();
    }

    private UpcomingFeatureResponse toResponse(
            Fixture fixture,
            Map<Long, PredictionFeature> featuresByFixtureId,
            Map<Long, GoalProbability>
                    goalProbabilitiesByFixtureId,
            Map<Long, Team> teamsById,
            Map<Long, League> leaguesById
    ) {
        PredictionFeature predictionFeature =
                featuresByFixtureId.get(
                        fixture.getId()
                );

        if (predictionFeature == null) {
            throw new FeatureReferenceException(
                    "PredictionFeature not found for fixture "
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
        
        GoalProbability goalProbability =
                goalProbabilitiesByFixtureId.get(
                        fixture.getId()
                );

        Map<String, BigDecimal> features =
                featureMapMapper.toMap(
                        predictionFeature,
                        goalProbability
                );

        return new UpcomingFeatureResponse(
                fixture.getId(),
                league.getId(),
                league.getName(),
                homeTeam.getId(),
                homeTeam.getName(),
                awayTeam.getId(),
                awayTeam.getName(),
                fixture.getDate(),
                features.size(),
                features
        );
    }

    private void validateRequest(
            LocalDateTime fromDate,
            LocalDateTime toDate,
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

        if (mode == null) {
            throw new IllegalArgumentException(
                    "mode must not be null"
            );
        }
    }
}