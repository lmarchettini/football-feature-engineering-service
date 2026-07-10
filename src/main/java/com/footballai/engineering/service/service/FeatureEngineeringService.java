package com.footballai.engineering.service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.footballai.engineering.service.entity.Fixture;
import com.footballai.engineering.service.entity.FixtureStatistic;
import com.footballai.engineering.service.entity.PredictionFeature;
import com.footballai.engineering.service.entity.Standing;
import com.footballai.engineering.service.repository.FixtureRepository;
import com.footballai.engineering.service.repository.FixtureStatisticRepository;
import com.footballai.engineering.service.repository.PredictionFeatureRepository;
import com.footballai.engineering.service.repository.StandingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureEngineeringService {

    private final FixtureRepository fixtureRepository;
    private final StandingRepository standingRepository;
    private final PredictionFeatureRepository predictionFeatureRepository;
    private final FixtureStatisticRepository fixtureStatisticRepository;

    @Transactional
    public void generateFeatures() {
        generateTrainingFeatures();
        generateUpcomingFeatures();
    }

    private void generateTrainingFeatures() {
        List<Fixture> fixtures = fixtureRepository.findFixturesWithoutFeatures(PageRequest.of(0, 100));

        log.info("Found {} trainable fixtures", fixtures.size());

        for (Fixture fixture : fixtures) {
            if (predictionFeatureRepository.existsById(fixture.getId())) {
                continue;
            }

            predictionFeatureRepository.save(buildTrainingFeature(fixture));
        }
    }

    private void generateUpcomingFeatures() {
        List<Fixture> fixtures = fixtureRepository.findUpcomingFixturesWithoutFeatures(PageRequest.of(0, 100));

        log.info("Found {} upcoming fixtures", fixtures.size());

        for (Fixture fixture : fixtures) {
            if (predictionFeatureRepository.existsById(fixture.getId())) {
                continue;
            }

            predictionFeatureRepository.save(buildUpcomingFeature(fixture));
        }
    }

    private PredictionFeature buildTrainingFeature(Fixture fixture) {

        TeamStats homeStats5 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 5);
        TeamStats awayStats5 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 5);

        TeamStats homeStats10 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 10);
        TeamStats awayStats10 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 10);

        TeamStats homeHomeStats = calculateHomeTeamStats(fixture.getHomeTeamId(), fixture, 5);
        TeamStats awayAwayStats = calculateAwayTeamStats(fixture.getAwayTeamId(), fixture, 5);

        int[] h2h = calculateHeadToHead(fixture, 5);

        Integer homeTablePosition = findTablePosition(
                fixture.getLeagueId(),
                fixture.getSeason(),
                fixture.getHomeTeamId()
        );

        Integer awayTablePosition = findTablePosition(
                fixture.getLeagueId(),
                fixture.getSeason(),
                fixture.getAwayTeamId()
        );

        int homeGoals = safeInt(fixture.getHomeGoals());
        int awayGoals = safeInt(fixture.getAwayGoals());
        int totalGoals = homeGoals + awayGoals;

        return PredictionFeature.builder()
                .fixtureId(fixture.getId())

                .homeTeamForm5(homeStats5.form())
                .awayTeamForm5(awayStats5.form())
                .homeTeamForm10(homeStats10.form())
                .awayTeamForm10(awayStats10.form())

                .homeAvgGoalsScored(homeStats5.avgGoalsScored())
                .awayAvgGoalsScored(awayStats5.avgGoalsScored())

                .homeAvgGoalsConceded(homeStats5.avgGoalsConceded())
                .awayAvgGoalsConceded(awayStats5.avgGoalsConceded())

                .homeWinRate(homeStats5.winRate())
                .awayWinRate(awayStats5.winRate())

                .homeCleanSheetRate(homeStats5.cleanSheetRate())
                .awayCleanSheetRate(awayStats5.cleanSheetRate())

                .homeRestDays(homeStats5.restDays())
                .awayRestDays(awayStats5.restDays())

                .h2hHomeWins(h2h[0])
                .h2hAwayWins(h2h[1])

                .homeTablePosition(homeTablePosition)
                .awayTablePosition(awayTablePosition)

                .homeAvgShots(homeStats5.avgShots())
                .awayAvgShots(awayStats5.avgShots())

                .homeAvgShotsOnGoal(homeStats5.avgShotsOnGoal())
                .awayAvgShotsOnGoal(awayStats5.avgShotsOnGoal())

                .homeAvgPossession(homeStats5.avgPossession())
                .awayAvgPossession(awayStats5.avgPossession())

                .homeAvgPassAccuracy(homeStats5.avgPassAccuracy())
                .awayAvgPassAccuracy(awayStats5.avgPassAccuracy())

                .homeAvgCorners(homeStats5.avgCorners())
                .awayAvgCorners(awayStats5.avgCorners())

                .homeAvgShotsInsideBox(homeStats5.avgShotsInsideBox())
                .awayAvgShotsInsideBox(awayStats5.avgShotsInsideBox())

                .homeAvgXg(homeStats5.avgXg())
                .awayAvgXg(awayStats5.avgXg())
                .homeAvgXga(homeStats5.avgXga())
                .awayAvgXga(awayStats5.avgXga())

                .homeHomeAvgGoalsScored(homeHomeStats.avgGoalsScored())
                .homeHomeAvgGoalsConceded(homeHomeStats.avgGoalsConceded())
                .homeHomeAvgShots(homeHomeStats.avgShots())
                .homeHomeAvgXg(homeHomeStats.avgXg())
                .homeHomeAvgXga(homeHomeStats.avgXga())

                .awayAwayAvgGoalsScored(awayAwayStats.avgGoalsScored())
                .awayAwayAvgGoalsConceded(awayAwayStats.avgGoalsConceded())
                .awayAwayAvgShots(awayAwayStats.avgShots())
                .awayAwayAvgXg(awayAwayStats.avgXg())
                .awayAwayAvgXga(awayAwayStats.avgXga())

                .oddsHome(null)
                .oddsDraw(null)
                .oddsAway(null)

                .targetGoal(totalGoals > 0)
                .targetOver25(totalGoals > 2)
                .targetBtts(homeGoals > 0 && awayGoals > 0)
                .targetHomeWin(homeGoals > awayGoals)

                .isTrainable(true)
                .featureVersion("v2")
                .build();
    }

    private PredictionFeature buildUpcomingFeature(Fixture fixture) {

        TeamStats homeStats5 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 5);
        TeamStats awayStats5 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 5);

        TeamStats homeStats10 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 10);
        TeamStats awayStats10 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 10);

        TeamStats homeHomeStats = calculateHomeTeamStats(fixture.getHomeTeamId(), fixture, 5);
        TeamStats awayAwayStats = calculateAwayTeamStats(fixture.getAwayTeamId(), fixture, 5);

        int[] h2h = calculateHeadToHead(fixture, 5);

        Integer homeTablePosition = findTablePosition(
                fixture.getLeagueId(),
                fixture.getSeason(),
                fixture.getHomeTeamId()
        );

        Integer awayTablePosition = findTablePosition(
                fixture.getLeagueId(),
                fixture.getSeason(),
                fixture.getAwayTeamId()
        );

        return PredictionFeature.builder()
                .fixtureId(fixture.getId())

                .homeTeamForm5(homeStats5.form())
                .awayTeamForm5(awayStats5.form())
                .homeTeamForm10(homeStats10.form())
                .awayTeamForm10(awayStats10.form())

                .homeAvgGoalsScored(homeStats5.avgGoalsScored())
                .awayAvgGoalsScored(awayStats5.avgGoalsScored())

                .homeAvgGoalsConceded(homeStats5.avgGoalsConceded())
                .awayAvgGoalsConceded(awayStats5.avgGoalsConceded())

                .homeWinRate(homeStats5.winRate())
                .awayWinRate(awayStats5.winRate())

                .homeCleanSheetRate(homeStats5.cleanSheetRate())
                .awayCleanSheetRate(awayStats5.cleanSheetRate())

                .homeRestDays(homeStats5.restDays())
                .awayRestDays(awayStats5.restDays())

                .h2hHomeWins(h2h[0])
                .h2hAwayWins(h2h[1])

                .homeTablePosition(homeTablePosition)
                .awayTablePosition(awayTablePosition)

                .homeAvgShots(homeStats5.avgShots())
                .awayAvgShots(awayStats5.avgShots())

                .homeAvgShotsOnGoal(homeStats5.avgShotsOnGoal())
                .awayAvgShotsOnGoal(awayStats5.avgShotsOnGoal())

                .homeAvgPossession(homeStats5.avgPossession())
                .awayAvgPossession(awayStats5.avgPossession())

                .homeAvgPassAccuracy(homeStats5.avgPassAccuracy())
                .awayAvgPassAccuracy(awayStats5.avgPassAccuracy())

                .homeAvgCorners(homeStats5.avgCorners())
                .awayAvgCorners(awayStats5.avgCorners())

                .homeAvgShotsInsideBox(homeStats5.avgShotsInsideBox())
                .awayAvgShotsInsideBox(awayStats5.avgShotsInsideBox())

                .homeAvgXg(homeStats5.avgXg())
                .awayAvgXg(awayStats5.avgXg())
                .homeAvgXga(homeStats5.avgXga())
                .awayAvgXga(awayStats5.avgXga())

                .homeHomeAvgGoalsScored(homeHomeStats.avgGoalsScored())
                .homeHomeAvgGoalsConceded(homeHomeStats.avgGoalsConceded())
                .homeHomeAvgShots(homeHomeStats.avgShots())
                .homeHomeAvgXg(homeHomeStats.avgXg())
                .homeHomeAvgXga(homeHomeStats.avgXga())

                .awayAwayAvgGoalsScored(awayAwayStats.avgGoalsScored())
                .awayAwayAvgGoalsConceded(awayAwayStats.avgGoalsConceded())
                .awayAwayAvgShots(awayAwayStats.avgShots())
                .awayAwayAvgXg(awayAwayStats.avgXg())
                .awayAwayAvgXga(awayAwayStats.avgXga())

                .oddsHome(null)
                .oddsDraw(null)
                .oddsAway(null)

                .targetGoal(null)
                .targetOver25(null)
                .targetBtts(null)
                .targetHomeWin(null)

                .isTrainable(false)
                .featureVersion("v2")
                .build();
    }

    private TeamStats calculateTeamStats(Long teamId, Fixture currentFixture, int lookback) {
        List<Fixture> matches = fixtureRepository.findPreviousMatches(
                teamId,
                currentFixture.getDate(),
                PageRequest.of(0, lookback)
        );

        return calculateStatsFromMatches(teamId, currentFixture, matches);
    }

    private TeamStats calculateHomeTeamStats(Long teamId, Fixture currentFixture, int lookback) {
        List<Fixture> matches = fixtureRepository.findPreviousHomeMatches(
                teamId,
                currentFixture.getDate(),
                PageRequest.of(0, lookback)
        );

        return calculateStatsFromMatches(teamId, currentFixture, matches);
    }

    private TeamStats calculateAwayTeamStats(Long teamId, Fixture currentFixture, int lookback) {
        List<Fixture> matches = fixtureRepository.findPreviousAwayMatches(
                teamId,
                currentFixture.getDate(),
                PageRequest.of(0, lookback)
        );

        return calculateStatsFromMatches(teamId, currentFixture, matches);
    }

    private TeamStats calculateStatsFromMatches(
            Long teamId,
            Fixture currentFixture,
            List<Fixture> previousMatches
    ) {
        if (previousMatches.isEmpty()) {
            return emptyTeamStats();
        }

        int points = 0;
        int goalsScored = 0;
        int goalsConceded = 0;
        int wins = 0;
        int cleanSheets = 0;

        int shotsTotal = 0;
        int shotsOnGoal = 0;
        int corners = 0;
        int shotsInsideBox = 0;

        BigDecimal possessionTotal = bd(0);
        BigDecimal passAccuracyTotal = bd(0);
        BigDecimal xgTotal = bd(0);
        BigDecimal xgaTotal = bd(0);

        int statsMatches = 0;

        for (Fixture match : previousMatches) {
            boolean isHome = match.getHomeTeamId().equals(teamId);

            int teamGoals = isHome ? safeInt(match.getHomeGoals()) : safeInt(match.getAwayGoals());
            int opponentGoals = isHome ? safeInt(match.getAwayGoals()) : safeInt(match.getHomeGoals());

            Long opponentTeamId = isHome ? match.getAwayTeamId() : match.getHomeTeamId();

            goalsScored += teamGoals;
            goalsConceded += opponentGoals;

            if (teamGoals > opponentGoals) {
                wins++;
                points += 3;
            } else if (teamGoals == opponentGoals) {
                points += 1;
            }

            if (opponentGoals == 0) {
                cleanSheets++;
            }

            Optional<FixtureStatistic> teamStatistic =
                    fixtureStatisticRepository.findByFixtureIdAndTeamId(match.getId(), teamId);

            Optional<FixtureStatistic> opponentStatistic =
                    fixtureStatisticRepository.findByFixtureIdAndTeamId(match.getId(), opponentTeamId);

            if (teamStatistic.isPresent()) {
                FixtureStatistic stat = teamStatistic.get();

                shotsTotal += safeInt(stat.getShotsTotal());
                shotsOnGoal += safeInt(stat.getShotsOnGoal());
                corners += safeInt(stat.getCorners());
                shotsInsideBox += safeInt(stat.getShotsInsideBox());

                possessionTotal = possessionTotal.add(safeDecimal(stat.getPossession()));
                passAccuracyTotal = passAccuracyTotal.add(safeDecimal(stat.getPassAccuracy()));
                xgTotal = xgTotal.add(safeDecimal(stat.getExpectedGoals()));

                if (opponentStatistic.isPresent()) {
                    xgaTotal = xgaTotal.add(
                            safeDecimal(opponentStatistic.get().getExpectedGoals())
                    );
                }

                statsMatches++;
            }
        }

        Integer restDays = null;
        Fixture lastMatch = previousMatches.get(0);

        if (lastMatch.getDate() != null && currentFixture.getDate() != null) {
            restDays = (int) Duration.between(
                    lastMatch.getDate(),
                    currentFixture.getDate()
            ).toDays();
        }

        int matches = previousMatches.size();

        return new TeamStats(
                divide(points, matches * 3),
                divide(goalsScored, matches),
                divide(goalsConceded, matches),
                divide(wins, matches),
                divide(cleanSheets, matches),
                restDays,
                divide(shotsTotal, statsMatches),
                divide(shotsOnGoal, statsMatches),
                divideDecimal(possessionTotal, statsMatches),
                divideDecimal(passAccuracyTotal, statsMatches),
                divide(corners, statsMatches),
                divide(shotsInsideBox, statsMatches),
                divideDecimal(xgTotal, statsMatches),
                divideDecimal(xgaTotal, statsMatches)
        );
    }

    private TeamStats emptyTeamStats() {
        return new TeamStats(
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                null,
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                bd(0),
                bd(0)
        );
    }

    private int[] calculateHeadToHead(Fixture fixture, int lookback) {
        List<Fixture> h2hMatches =
                fixtureRepository.findPreviousHeadToHead(
                        fixture.getHomeTeamId(),
                        fixture.getAwayTeamId(),
                        fixture.getDate(),
                        PageRequest.of(0, lookback)
                );

        int homeWins = 0;
        int awayWins = 0;

        for (Fixture match : h2hMatches) {
            int homeGoals = safeInt(match.getHomeGoals());
            int awayGoals = safeInt(match.getAwayGoals());

            if (homeGoals == awayGoals) {
                continue;
            }

            Long winnerTeamId = homeGoals > awayGoals
                    ? match.getHomeTeamId()
                    : match.getAwayTeamId();

            if (winnerTeamId.equals(fixture.getHomeTeamId())) {
                homeWins++;
            }

            if (winnerTeamId.equals(fixture.getAwayTeamId())) {
                awayWins++;
            }
        }

        return new int[]{homeWins, awayWins};
    }

    private Integer findTablePosition(Long leagueId, Integer season, Long teamId) {
        Optional<Standing> standing =
                standingRepository.findByLeagueIdAndSeasonAndTeamId(
                        leagueId,
                        season,
                        teamId
                );

        return standing.map(Standing::getPosition).orElse(null);
    }

    private BigDecimal divide(int value, int divisor) {
        if (divisor == 0) {
            return bd(0);
        }

        return BigDecimal.valueOf(value)
                .divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal divideDecimal(BigDecimal value, int divisor) {
        if (divisor == 0) {
            return bd(0);
        }

        return value.divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal bd(int value) {
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP);
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private BigDecimal safeDecimal(BigDecimal value) {
        return value == null ? bd(0) : value;
    }
}