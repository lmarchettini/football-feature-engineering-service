package com.footballai.engineering.service.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.footballai.engineering.service.config.FeatureEngineeringProperties;
import com.footballai.engineering.service.entity.Fixture;
import com.footballai.engineering.service.entity.FixtureStatistic;
import com.footballai.engineering.service.entity.PredictionFeature;
import com.footballai.engineering.service.repository.FixtureRepository;
import com.footballai.engineering.service.repository.FixtureStatisticRepository;
import com.footballai.engineering.service.repository.PredictionFeatureRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureEngineeringService {

	private final FixtureRepository fixtureRepository;
	private final HistoricalLeagueTableService historicalLeagueTableService;
	private final PredictionFeatureRepository predictionFeatureRepository;
	private final FixtureStatisticRepository fixtureStatisticRepository;
	private final FeatureEngineeringProperties properties;

	@Transactional
	public void generateFeatures() {
		generateTrainingFeatures();
		generateUpcomingFeatures();
	}

	private void generateTrainingFeatures() {
		List<Fixture> fixtures = fixtureRepository
				.findFixturesWithoutFeatures(PageRequest.of(0, properties.getBatchSize()));

		if (fixtures.isEmpty()) {
			log.debug("No trainable fixtures found");
		} else {
			log.info("Found {} trainable fixtures", fixtures.size());
		}

		for (Fixture fixture : fixtures) {
			if (predictionFeatureRepository.existsById(fixture.getId())) {
				continue;
			}

			predictionFeatureRepository.save(buildTrainingFeature(fixture));
		}
	}

	private void generateUpcomingFeatures() {
		List<Fixture> fixtures = fixtureRepository
				.findUpcomingFixturesWithoutFeatures(PageRequest.of(0, properties.getBatchSize()));

		if (fixtures.isEmpty()) {
			log.debug("No upcoming fixtures found");
		} else {
			log.info("Found {} upcoming fixtures", fixtures.size());
		}

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

		BttsInteractionFeatures bttsInteractionFeatures = calculateBttsInteractionFeatures(
				homeStats5, 
				awayStats5,
				homeStats10, 
				awayStats10, 
				homeHomeStats, 
				awayAwayStats);

		BigDecimal combinedAvgXg = calculateCombinedAvgXg(homeStats5, awayStats5);

		BigDecimal expectedMatchGoals = calculateExpectedMatchGoals(homeStats5, awayStats5);

		BigDecimal formDifference = calculateDifference(homeStats5.form(), awayStats5.form());

		BigDecimal xgDifference = calculateDifference(homeStats5.avgXg(), awayStats5.avgXg());

		BigDecimal xgaDifference = calculateDifference(homeStats5.avgXga(), awayStats5.avgXga());

		BigDecimal goalsScoredDifference = calculateDifference(homeStats5.avgGoalsScored(),
				awayStats5.avgGoalsScored());

		BigDecimal goalsConcededDifference = calculateDifference(homeStats5.avgGoalsConceded(),
				awayStats5.avgGoalsConceded());

		BigDecimal shotsDifference = calculateDifference(homeStats5.avgShots(), awayStats5.avgShots());

		BigDecimal shotsOnGoalDifference = calculateDifference(homeStats5.avgShotsOnGoal(),
				awayStats5.avgShotsOnGoal());

		BigDecimal possessionDifference = calculateDifference(homeStats5.avgPossession(), awayStats5.avgPossession());

		BigDecimal cornersDifference = calculateDifference(homeStats5.avgCorners(), awayStats5.avgCorners());

		BigDecimal passAccuracyDifference = calculateDifference(homeStats5.avgPassAccuracy(),
				awayStats5.avgPassAccuracy());

		int[] h2h = calculateHeadToHead(fixture, 5);

		Map<Long, Integer> tablePositions = historicalLeagueTableService.calculatePositionsBefore(fixture);

		Integer homeTablePosition = tablePositions.get(fixture.getHomeTeamId());

		Integer awayTablePosition = tablePositions.get(fixture.getAwayTeamId());

		int homeGoals = safeInt(fixture.getHomeGoals());
		int awayGoals = safeInt(fixture.getAwayGoals());
		int totalGoals = homeGoals + awayGoals;

		return PredictionFeature.builder().fixtureId(fixture.getId())

				.homeTeamForm5(homeStats5.form()).awayTeamForm5(awayStats5.form()).homeTeamForm10(homeStats10.form())
				.awayTeamForm10(awayStats10.form())

				.homeAvgGoalsScored(homeStats5.avgGoalsScored()).awayAvgGoalsScored(awayStats5.avgGoalsScored())

				.homeAvgGoalsConceded(homeStats5.avgGoalsConceded()).awayAvgGoalsConceded(awayStats5.avgGoalsConceded())

				.homeWinRate(homeStats5.winRate()).awayWinRate(awayStats5.winRate())

				.homeCleanSheetRate(homeStats5.cleanSheetRate()).awayCleanSheetRate(awayStats5.cleanSheetRate())

				.homeRestDays(homeStats5.restDays()).awayRestDays(awayStats5.restDays())

				.h2hHomeWins(h2h[0]).h2hAwayWins(h2h[1])

				.homeTablePosition(homeTablePosition).awayTablePosition(awayTablePosition)

				.homeAvgShots(homeStats5.avgShots()).awayAvgShots(awayStats5.avgShots())

				.homeAvgShotsOnGoal(homeStats5.avgShotsOnGoal()).awayAvgShotsOnGoal(awayStats5.avgShotsOnGoal())

				.homeAvgPossession(homeStats5.avgPossession()).awayAvgPossession(awayStats5.avgPossession())

				.homeAvgPassAccuracy(homeStats5.avgPassAccuracy()).awayAvgPassAccuracy(awayStats5.avgPassAccuracy())

				.homeAvgCorners(homeStats5.avgCorners()).awayAvgCorners(awayStats5.avgCorners())

				.homeAvgShotsInsideBox(homeStats5.avgShotsInsideBox())
				.awayAvgShotsInsideBox(awayStats5.avgShotsInsideBox())

				.homeAvgXg(homeStats5.avgXg()).awayAvgXg(awayStats5.avgXg()).homeAvgXga(homeStats5.avgXga())
				.awayAvgXga(awayStats5.avgXga())

				.homeBttsRate5(homeStats5.bttsRate()).awayBttsRate5(awayStats5.bttsRate())
				.homeBttsRate10(homeStats10.bttsRate()).awayBttsRate10(awayStats10.bttsRate())

				.homeOver25Rate5(homeStats5.over25Rate()).awayOver25Rate5(awayStats5.over25Rate())
				.homeOver25Rate10(homeStats10.over25Rate()).awayOver25Rate10(awayStats10.over25Rate())
				.homeScoredRate5(homeStats5.scoredRate()).awayScoredRate5(awayStats5.scoredRate())

				.homeScoredRate10(homeStats10.scoredRate()).awayScoredRate10(awayStats10.scoredRate())

				.homeConcededRate5(homeStats5.concededRate()).awayConcededRate5(awayStats5.concededRate())

				.homeConcededRate10(homeStats10.concededRate()).awayConcededRate10(awayStats10.concededRate())

				.homeAvgTotalGoals5(homeStats5.avgTotalGoals()).awayAvgTotalGoals5(awayStats5.avgTotalGoals())

				.homeAvgTotalGoals10(homeStats10.avgTotalGoals()).awayAvgTotalGoals10(awayStats10.avgTotalGoals())

				.expectedHomeGoals(average(homeHomeStats.avgXg(), awayAwayStats.avgXga()))

				.expectedAwayGoals(average(awayAwayStats.avgXg(), homeHomeStats.avgXga()))
				
				.estimatedHomeGoals(
						bttsInteractionFeatures.estimatedHomeGoals()
				)

				.estimatedAwayGoals(
						bttsInteractionFeatures.estimatedAwayGoals()
				)

				.minExpectedGoals(bttsInteractionFeatures.minExpectedGoals())

				.maxExpectedGoals(bttsInteractionFeatures.maxExpectedGoals())

				.expectedGoalsGap(bttsInteractionFeatures.expectedGoalsGap())

				.expectedGoalsProduct(bttsInteractionFeatures.expectedGoalsProduct())

				.minScoredRate5(bttsInteractionFeatures.minScoredRate5())

				.minScoredRate10(bttsInteractionFeatures.minScoredRate10())

				.scoredRateProduct10(bttsInteractionFeatures.scoredRateProduct10())

				.minConcededRate5(bttsInteractionFeatures.minConcededRate5())

				.minConcededRate10(bttsInteractionFeatures.minConcededRate10())

				.concededRateProduct10(bttsInteractionFeatures.concededRateProduct10())

				.homeAttackVsAwayDefence(bttsInteractionFeatures.homeAttackVsAwayDefence())

				.awayAttackVsHomeDefence(bttsInteractionFeatures.awayAttackVsHomeDefence())

				.combinedAvgXg(combinedAvgXg).expectedMatchGoals(expectedMatchGoals)

				// ===== V5: DIFFERENCE FEATURES =====

				.formDifference(formDifference).xgDifference(xgDifference).xgaDifference(xgaDifference)
				.goalsScoredDifference(goalsScoredDifference).goalsConcededDifference(goalsConcededDifference)
				.shotsDifference(shotsDifference).shotsOnGoalDifference(shotsOnGoalDifference)
				.possessionDifference(possessionDifference).cornersDifference(cornersDifference)
				.passAccuracyDifference(passAccuracyDifference)

				.homeHomeAvgGoalsScored(homeHomeStats.avgGoalsScored())
				.homeHomeAvgGoalsConceded(homeHomeStats.avgGoalsConceded()).homeHomeAvgShots(homeHomeStats.avgShots())
				.homeHomeAvgXg(homeHomeStats.avgXg()).homeHomeAvgXga(homeHomeStats.avgXga())

				.awayAwayAvgGoalsScored(awayAwayStats.avgGoalsScored())
				.awayAwayAvgGoalsConceded(awayAwayStats.avgGoalsConceded()).awayAwayAvgShots(awayAwayStats.avgShots())
				.awayAwayAvgXg(awayAwayStats.avgXg()).awayAwayAvgXga(awayAwayStats.avgXga())

				.oddsHome(null).oddsDraw(null).oddsAway(null)

				.targetGoal(totalGoals > 0)

				.targetOver15(totalGoals > 1)

				.targetOver25(totalGoals > 2)

				.targetUnder45(totalGoals < 5)

				.targetBtts(homeGoals > 0 && awayGoals > 0)

				.targetHomeWin(homeGoals > awayGoals)
				
				.targetHomeScored(homeGoals > 0)
				.targetAwayScored(awayGoals > 0)

				.targetDoubleChance1x(homeGoals >= awayGoals)

				.targetDoubleChanceX2(awayGoals >= homeGoals)

				.targetDoubleChance12(homeGoals != awayGoals)

				.isTrainable(true).build();
	}

	private PredictionFeature buildUpcomingFeature(Fixture fixture) {

		TeamStats homeStats5 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 5);
		TeamStats awayStats5 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 5);

		TeamStats homeStats10 = calculateTeamStats(fixture.getHomeTeamId(), fixture, 10);
		TeamStats awayStats10 = calculateTeamStats(fixture.getAwayTeamId(), fixture, 10);

		TeamStats homeHomeStats = calculateHomeTeamStats(fixture.getHomeTeamId(), fixture, 5);
		TeamStats awayAwayStats = calculateAwayTeamStats(fixture.getAwayTeamId(), fixture, 5);

		BttsInteractionFeatures bttsInteractionFeatures = calculateBttsInteractionFeatures(homeStats5, awayStats5,
				homeStats10, awayStats10, homeHomeStats, awayAwayStats);

		BigDecimal combinedAvgXg = calculateCombinedAvgXg(homeStats5, awayStats5);

		BigDecimal expectedMatchGoals = calculateExpectedMatchGoals(homeStats5, awayStats5);

		BigDecimal formDifference = calculateDifference(homeStats5.form(), awayStats5.form());

		BigDecimal xgDifference = calculateDifference(homeStats5.avgXg(), awayStats5.avgXg());

		BigDecimal xgaDifference = calculateDifference(homeStats5.avgXga(), awayStats5.avgXga());

		BigDecimal goalsScoredDifference = calculateDifference(homeStats5.avgGoalsScored(),
				awayStats5.avgGoalsScored());

		BigDecimal goalsConcededDifference = calculateDifference(homeStats5.avgGoalsConceded(),
				awayStats5.avgGoalsConceded());

		BigDecimal shotsDifference = calculateDifference(homeStats5.avgShots(), awayStats5.avgShots());

		BigDecimal shotsOnGoalDifference = calculateDifference(homeStats5.avgShotsOnGoal(),
				awayStats5.avgShotsOnGoal());

		BigDecimal possessionDifference = calculateDifference(homeStats5.avgPossession(), awayStats5.avgPossession());

		BigDecimal cornersDifference = calculateDifference(homeStats5.avgCorners(), awayStats5.avgCorners());

		BigDecimal passAccuracyDifference = calculateDifference(homeStats5.avgPassAccuracy(),
				awayStats5.avgPassAccuracy());

		int[] h2h = calculateHeadToHead(fixture, 5);

		Map<Long, Integer> tablePositions = historicalLeagueTableService.calculatePositionsBefore(fixture);

		Integer homeTablePosition = tablePositions.get(fixture.getHomeTeamId());

		Integer awayTablePosition = tablePositions.get(fixture.getAwayTeamId());

		return PredictionFeature.builder().fixtureId(fixture.getId())

				.homeTeamForm5(homeStats5.form()).awayTeamForm5(awayStats5.form()).homeTeamForm10(homeStats10.form())
				.awayTeamForm10(awayStats10.form())

				.homeAvgGoalsScored(homeStats5.avgGoalsScored()).awayAvgGoalsScored(awayStats5.avgGoalsScored())

				.homeAvgGoalsConceded(homeStats5.avgGoalsConceded()).awayAvgGoalsConceded(awayStats5.avgGoalsConceded())

				.homeWinRate(homeStats5.winRate()).awayWinRate(awayStats5.winRate())

				.homeCleanSheetRate(homeStats5.cleanSheetRate()).awayCleanSheetRate(awayStats5.cleanSheetRate())

				.homeRestDays(homeStats5.restDays()).awayRestDays(awayStats5.restDays())

				.h2hHomeWins(h2h[0]).h2hAwayWins(h2h[1])

				.homeTablePosition(homeTablePosition).awayTablePosition(awayTablePosition)

				.homeAvgShots(homeStats5.avgShots()).awayAvgShots(awayStats5.avgShots())

				.homeAvgShotsOnGoal(homeStats5.avgShotsOnGoal()).awayAvgShotsOnGoal(awayStats5.avgShotsOnGoal())

				.homeAvgPossession(homeStats5.avgPossession()).awayAvgPossession(awayStats5.avgPossession())

				.homeAvgPassAccuracy(homeStats5.avgPassAccuracy()).awayAvgPassAccuracy(awayStats5.avgPassAccuracy())

				.homeAvgCorners(homeStats5.avgCorners()).awayAvgCorners(awayStats5.avgCorners())

				.homeAvgShotsInsideBox(homeStats5.avgShotsInsideBox())
				.awayAvgShotsInsideBox(awayStats5.avgShotsInsideBox())

				.homeAvgXg(homeStats5.avgXg()).awayAvgXg(awayStats5.avgXg()).homeAvgXga(homeStats5.avgXga())
				.awayAvgXga(awayStats5.avgXga())

				.homeHomeAvgGoalsScored(homeHomeStats.avgGoalsScored())
				.homeHomeAvgGoalsConceded(homeHomeStats.avgGoalsConceded()).homeHomeAvgShots(homeHomeStats.avgShots())
				.homeHomeAvgXg(homeHomeStats.avgXg()).homeHomeAvgXga(homeHomeStats.avgXga())

				.awayAwayAvgGoalsScored(awayAwayStats.avgGoalsScored())
				.awayAwayAvgGoalsConceded(awayAwayStats.avgGoalsConceded()).awayAwayAvgShots(awayAwayStats.avgShots())
				.awayAwayAvgXg(awayAwayStats.avgXg()).awayAwayAvgXga(awayAwayStats.avgXga())

				.homeBttsRate5(homeStats5.bttsRate()).awayBttsRate5(awayStats5.bttsRate())
				.homeBttsRate10(homeStats10.bttsRate()).awayBttsRate10(awayStats10.bttsRate())

				.homeOver25Rate5(homeStats5.over25Rate()).awayOver25Rate5(awayStats5.over25Rate())
				.homeOver25Rate10(homeStats10.over25Rate()).awayOver25Rate10(awayStats10.over25Rate())

				.homeScoredRate5(homeStats5.scoredRate()).awayScoredRate5(awayStats5.scoredRate())

				.homeScoredRate10(homeStats10.scoredRate()).awayScoredRate10(awayStats10.scoredRate())

				.homeConcededRate5(homeStats5.concededRate()).awayConcededRate5(awayStats5.concededRate())

				.homeConcededRate10(homeStats10.concededRate()).awayConcededRate10(awayStats10.concededRate())

				.homeAvgTotalGoals5(homeStats5.avgTotalGoals()).awayAvgTotalGoals5(awayStats5.avgTotalGoals())

				.homeAvgTotalGoals10(homeStats10.avgTotalGoals()).awayAvgTotalGoals10(awayStats10.avgTotalGoals())

				.expectedHomeGoals(average(homeHomeStats.avgXg(), awayAwayStats.avgXga()))

				.expectedAwayGoals(average(awayAwayStats.avgXg(), homeHomeStats.avgXga()))
				
				.estimatedHomeGoals(
						bttsInteractionFeatures.estimatedHomeGoals()
				)

				.estimatedAwayGoals(
						bttsInteractionFeatures.estimatedAwayGoals()
				)

				.minExpectedGoals(bttsInteractionFeatures.minExpectedGoals())

				.maxExpectedGoals(bttsInteractionFeatures.maxExpectedGoals())

				.expectedGoalsGap(bttsInteractionFeatures.expectedGoalsGap())

				.expectedGoalsProduct(bttsInteractionFeatures.expectedGoalsProduct())

				.minScoredRate5(bttsInteractionFeatures.minScoredRate5())

				.minScoredRate10(bttsInteractionFeatures.minScoredRate10())

				.scoredRateProduct10(bttsInteractionFeatures.scoredRateProduct10())

				.minConcededRate5(bttsInteractionFeatures.minConcededRate5())

				.minConcededRate10(bttsInteractionFeatures.minConcededRate10())

				.concededRateProduct10(bttsInteractionFeatures.concededRateProduct10())

				.homeAttackVsAwayDefence(bttsInteractionFeatures.homeAttackVsAwayDefence())

				.awayAttackVsHomeDefence(bttsInteractionFeatures.awayAttackVsHomeDefence())

				.combinedAvgXg(combinedAvgXg).expectedMatchGoals(expectedMatchGoals)

				.formDifference(formDifference).xgDifference(xgDifference).xgaDifference(xgaDifference)
				.goalsScoredDifference(goalsScoredDifference).goalsConcededDifference(goalsConcededDifference)
				.shotsDifference(shotsDifference).shotsOnGoalDifference(shotsOnGoalDifference)
				.possessionDifference(possessionDifference).cornersDifference(cornersDifference)
				.passAccuracyDifference(passAccuracyDifference)

				.oddsHome(null).oddsDraw(null).oddsAway(null)

				.targetGoal(null).targetOver15(null).targetOver25(null).targetUnder45(null).targetBtts(null)
				.targetHomeScored(null)
				.targetAwayScored(null)
				.targetHomeWin(null).targetDoubleChance1x(null).targetDoubleChanceX2(null).targetDoubleChance12(null)

				.isTrainable(false).build();
	}

	private TeamStats calculateTeamStats(Long teamId, Fixture currentFixture, int lookback) {
		List<Fixture> matches = fixtureRepository.findPreviousMatches(teamId, currentFixture.getDate(),
				PageRequest.of(0, lookback));

		return calculateStatsFromMatches(teamId, currentFixture, matches);
	}

	private TeamStats calculateHomeTeamStats(Long teamId, Fixture currentFixture, int lookback) {
		List<Fixture> matches = fixtureRepository.findPreviousHomeMatches(teamId, currentFixture.getDate(),
				PageRequest.of(0, lookback));

		return calculateStatsFromMatches(teamId, currentFixture, matches);
	}

	private TeamStats calculateAwayTeamStats(Long teamId, Fixture currentFixture, int lookback) {
		List<Fixture> matches = fixtureRepository.findPreviousAwayMatches(teamId, currentFixture.getDate(),
				PageRequest.of(0, lookback));

		return calculateStatsFromMatches(teamId, currentFixture, matches);
	}

	private TeamStats calculateStatsFromMatches(Long teamId, Fixture currentFixture, List<Fixture> previousMatches) {

		if (previousMatches.isEmpty()) {
			return emptyTeamStats();
		}

		int points = 0;
		int goalsScored = 0;
		int goalsConceded = 0;
		int wins = 0;
		int cleanSheets = 0;

		int scoredMatches = 0;

		int concededMatches = 0;

		int bttsMatches = 0;
		int over25Matches = 0;

		int shotsTotal = 0;
		int shotsOnGoal = 0;
		int corners = 0;
		int shotsInsideBox = 0;

		BigDecimal possessionTotal = bd(0);
		BigDecimal passAccuracyTotal = bd(0);
		BigDecimal xgTotal = bd(0);
		BigDecimal xgaTotal = bd(0);

		int statsMatches = 0;

		/*
		 * Questi contatori sono separati perché expected_goals può essere assente anche
		 * quando le altre statistiche della partita sono presenti.
		 */
		int xgMatches = 0;
		int xgaMatches = 0;

		List<Long> fixtureIds = previousMatches.stream().map(Fixture::getId).toList();

		Map<String, FixtureStatistic> statisticsByFixtureAndTeam = fixtureStatisticRepository
				.findByFixtureIdIn(fixtureIds).stream()
				.collect(Collectors.toMap(statistic -> statistic.getFixtureId() + ":" + statistic.getTeamId(),
						Function.identity(), (first, second) -> first));

		for (Fixture match : previousMatches) {

			boolean isHome = match.getHomeTeamId().equals(teamId);

			int matchHomeGoals = safeInt(match.getHomeGoals());

			int matchAwayGoals = safeInt(match.getAwayGoals());

			int teamGoals = isHome ? matchHomeGoals : matchAwayGoals;

			int opponentGoals = isHome ? matchAwayGoals : matchHomeGoals;

			Long opponentTeamId = isHome ? match.getAwayTeamId() : match.getHomeTeamId();

			goalsScored += teamGoals;
			goalsConceded += opponentGoals;

			if (teamGoals > 0) {
				scoredMatches++;
			}

			if (opponentGoals > 0) {
				concededMatches++;
			}

			if (teamGoals > opponentGoals) {
				wins++;
				points += 3;
			} else if (teamGoals == opponentGoals) {
				points += 1;
			}

			if (opponentGoals == 0) {
				cleanSheets++;
			}

			/*
			 * BTTS: entrambe le squadre hanno segnato.
			 */
			if (matchHomeGoals > 0 && matchAwayGoals > 0) {
				bttsMatches++;
			}

			/*
			 * Over 2.5: almeno 3 gol complessivi.
			 */
			if (matchHomeGoals + matchAwayGoals > 2) {
				over25Matches++;
			}

			FixtureStatistic teamStatistic = statisticsByFixtureAndTeam.get(match.getId() + ":" + teamId);

			FixtureStatistic opponentStatistic = statisticsByFixtureAndTeam.get(match.getId() + ":" + opponentTeamId);

			if (teamStatistic != null) {

				shotsTotal += safeInt(teamStatistic.getShotsTotal());

				shotsOnGoal += safeInt(teamStatistic.getShotsOnGoal());

				corners += safeInt(teamStatistic.getCorners());

				shotsInsideBox += safeInt(teamStatistic.getShotsInsideBox());

				possessionTotal = possessionTotal.add(safeDecimal(teamStatistic.getPossession()));

				passAccuracyTotal = passAccuracyTotal.add(safeDecimal(teamStatistic.getPassAccuracy()));

				/*
				 * Non convertiamo un expected_goals mancante in una reale osservazione uguale a
				 * zero.
				 */
				if (teamStatistic.getExpectedGoals() != null) {

					xgTotal = xgTotal.add(teamStatistic.getExpectedGoals());

					xgMatches++;
				}

				if (opponentStatistic != null && opponentStatistic.getExpectedGoals() != null) {

					xgaTotal = xgaTotal.add(opponentStatistic.getExpectedGoals());

					xgaMatches++;
				}

				statsMatches++;
			}
		}

		int restDays = 5;

		Fixture lastMatch = previousMatches.get(0);

		if (lastMatch.getDate() != null && currentFixture.getDate() != null) {

			long calculatedRestDays = Duration.between(lastMatch.getDate(), currentFixture.getDate()).toDays();

			restDays = (int) Math.max(0, Math.min(calculatedRestDays, 30));
		}

		if (lastMatch.getDate() != null && currentFixture.getDate() != null) {

			restDays = (int) Duration.between(lastMatch.getDate(), currentFixture.getDate()).toDays();
		}

		int matches = previousMatches.size();

		return new TeamStats(divide(points, matches * 3),

				divide(goalsScored, matches),

				divide(goalsConceded, matches),

				// Media dei gol totali prodotti nelle partite della squadra:
				// gol segnati + gol subiti.
				divide(goalsScored + goalsConceded, matches),

				divide(wins, matches),

				divide(cleanSheets, matches),

				// Percentuale di partite in cui la squadra ha segnato almeno un gol.
				divide(scoredMatches, matches),

				// Percentuale di partite in cui la squadra ha subito almeno un gol.
				divide(concededMatches, matches),

				restDays,

				divide(shotsTotal, statsMatches),

				divide(shotsOnGoal, statsMatches),

				divideDecimal(possessionTotal, statsMatches),

				divideDecimal(passAccuracyTotal, statsMatches),

				divide(corners, statsMatches),

				divide(shotsInsideBox, statsMatches),

				/*
				 * Gli xG vengono mediati soltanto sulle partite dove il dato è realmente
				 * disponibile.
				 */
				divideDecimal(xgTotal, xgMatches),

				divideDecimal(xgaTotal, xgaMatches),

				divide(bttsMatches, matches),

				divide(over25Matches, matches));
	}

	private TeamStats emptyTeamStats() {

		return new TeamStats(bd(0), // form
				bd(0), // avgGoalsScored
				bd(0), // avgGoalsConceded
				bd(0), // avgTotalGoals
				bd(0), // winRate
				bd(0), // cleanSheetRate
				bd(0), // scoredRate
				bd(0), // concededRate
				5, // restDays
				bd(0), // avgShots
				bd(0), // avgShotsOnGoal
				bd(0), // avgPossession
				bd(0), // avgPassAccuracy
				bd(0), // avgCorners
				bd(0), // avgShotsInsideBox
				bd(0), // avgXg
				bd(0), // avgXga
				bd(0), // bttsRate
				bd(0) // over25Rate
		);
	}

	private int[] calculateHeadToHead(Fixture fixture, int lookback) {
		List<Fixture> h2hMatches = fixtureRepository.findPreviousHeadToHead(fixture.getHomeTeamId(),
				fixture.getAwayTeamId(), fixture.getDate(), PageRequest.of(0, lookback));

		int homeWins = 0;
		int awayWins = 0;

		for (Fixture match : h2hMatches) {
			int homeGoals = safeInt(match.getHomeGoals());
			int awayGoals = safeInt(match.getAwayGoals());

			if (homeGoals == awayGoals) {
				continue;
			}

			Long winnerTeamId = homeGoals > awayGoals ? match.getHomeTeamId() : match.getAwayTeamId();

			if (winnerTeamId.equals(fixture.getHomeTeamId())) {
				homeWins++;
			}

			if (winnerTeamId.equals(fixture.getAwayTeamId())) {
				awayWins++;
			}
		}

		return new int[] { homeWins, awayWins };
	}

	private BigDecimal divide(int value, int divisor) {
		if (divisor == 0) {
			return bd(0);
		}

		return BigDecimal.valueOf(value).divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP);
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

	private BigDecimal calculateCombinedAvgXg(TeamStats homeStats, TeamStats awayStats) {

		return safeDecimal(homeStats.avgXg()).add(safeDecimal(awayStats.avgXg())).setScale(4, RoundingMode.HALF_UP);
	}

	private BigDecimal calculateExpectedMatchGoals(TeamStats homeStats, TeamStats awayStats) {

		BigDecimal expectedHomeGoals = average(homeStats.avgXg(), awayStats.avgXga());

		BigDecimal expectedAwayGoals = average(awayStats.avgXg(), homeStats.avgXga());

		return expectedHomeGoals.add(expectedAwayGoals).setScale(4, RoundingMode.HALF_UP);
	}

	private BigDecimal average(BigDecimal first, BigDecimal second) {

		return safeDecimal(first).add(safeDecimal(second)).divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP);
	}

	private BigDecimal calculateDifference(BigDecimal homeValue, BigDecimal awayValue) {

		return safeDecimal(homeValue).subtract(safeDecimal(awayValue)).setScale(4, RoundingMode.HALF_UP);
	}

	private BttsInteractionFeatures calculateBttsInteractionFeatures(TeamStats homeStats5, TeamStats awayStats5,
			TeamStats homeStats10, TeamStats awayStats10, TeamStats homeHomeStats, TeamStats awayAwayStats) {

		BigDecimal estimatedHomeGoals = calculateEstimatedGoals(homeHomeStats.avgXg(), awayAwayStats.avgXga(),
				homeHomeStats.avgGoalsScored(), awayAwayStats.avgGoalsConceded());

		BigDecimal estimatedAwayGoals = calculateEstimatedGoals(awayAwayStats.avgXg(), homeHomeStats.avgXga(),
				awayAwayStats.avgGoalsScored(), homeHomeStats.avgGoalsConceded());

		BigDecimal minExpectedGoals = minimum(estimatedHomeGoals, estimatedAwayGoals);

		BigDecimal maxExpectedGoals = maximum(estimatedHomeGoals, estimatedAwayGoals);

		BigDecimal expectedGoalsGap = absoluteDifference(estimatedHomeGoals, estimatedAwayGoals);

		BigDecimal expectedGoalsProduct = multiply(estimatedHomeGoals, estimatedAwayGoals);

		BigDecimal minScoredRate5 = minimum(homeStats5.scoredRate(), awayStats5.scoredRate());

		BigDecimal minScoredRate10 = minimum(homeStats10.scoredRate(), awayStats10.scoredRate());

		BigDecimal scoredRateProduct10 = multiply(homeStats10.scoredRate(), awayStats10.scoredRate());

		BigDecimal minConcededRate5 = minimum(homeStats5.concededRate(), awayStats5.concededRate());

		BigDecimal minConcededRate10 = minimum(homeStats10.concededRate(), awayStats10.concededRate());

		BigDecimal concededRateProduct10 = multiply(homeStats10.concededRate(), awayStats10.concededRate());

		/*
		 * Prestazioni split: attacco casalingo contro difesa esterna; attacco esterno
		 * contro difesa casalinga.
		 */
		BigDecimal homeAttackVsAwayDefence = multiply(homeHomeStats.avgGoalsScored(), awayAwayStats.avgGoalsConceded());

		BigDecimal awayAttackVsHomeDefence = multiply(awayAwayStats.avgGoalsScored(), homeHomeStats.avgGoalsConceded());

		return new BttsInteractionFeatures(estimatedHomeGoals, estimatedAwayGoals, minExpectedGoals, maxExpectedGoals,
				expectedGoalsGap, expectedGoalsProduct, minScoredRate5, minScoredRate10, scoredRateProduct10,
				minConcededRate5, minConcededRate10, concededRateProduct10, homeAttackVsAwayDefence,
				awayAttackVsHomeDefence);
	}

	private BigDecimal calculateEstimatedGoals(BigDecimal attackingXg, BigDecimal defendingXga,
			BigDecimal averageGoalsScored, BigDecimal opponentAverageGoalsConceded) {

		if (isPositive(attackingXg) && isPositive(defendingXga)) {

			return average(attackingXg, defendingXga);
		}

		return average(averageGoalsScored, opponentAverageGoalsConceded);
	}
	
	private boolean isPositive(BigDecimal value) {
	    return value != null
	            && value.compareTo(BigDecimal.ZERO) > 0;
	}
	
	private BigDecimal minimum(
	        BigDecimal first,
	        BigDecimal second
	) {
	    return safeDecimal(first)
	            .min(safeDecimal(second))
	            .setScale(
	                    4,
	                    RoundingMode.HALF_UP
	            );
	}

	private BigDecimal maximum(
	        BigDecimal first,
	        BigDecimal second
	) {
	    return safeDecimal(first)
	            .max(safeDecimal(second))
	            .setScale(
	                    4,
	                    RoundingMode.HALF_UP
	            );
	}

	private BigDecimal absoluteDifference(
	        BigDecimal first,
	        BigDecimal second
	) {
	    return safeDecimal(first)
	            .subtract(safeDecimal(second))
	            .abs()
	            .setScale(
	                    4,
	                    RoundingMode.HALF_UP
	            );
	}

	private BigDecimal multiply(
	        BigDecimal first,
	        BigDecimal second
	) {
	    return safeDecimal(first)
	            .multiply(safeDecimal(second))
	            .setScale(
	                    4,
	                    RoundingMode.HALF_UP
	            );
	}

	private record BttsInteractionFeatures(BigDecimal estimatedHomeGoals, BigDecimal estimatedAwayGoals,
			BigDecimal minExpectedGoals, BigDecimal maxExpectedGoals, BigDecimal expectedGoalsGap,
			BigDecimal expectedGoalsProduct, BigDecimal minScoredRate5, BigDecimal minScoredRate10,
			BigDecimal scoredRateProduct10, BigDecimal minConcededRate5, BigDecimal minConcededRate10,
			BigDecimal concededRateProduct10, BigDecimal homeAttackVsAwayDefence, BigDecimal awayAttackVsHomeDefence) {
	}
}