package com.footballai.engineering.service.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.footballai.engineering.service.entity.Fixture;
import com.footballai.engineering.service.repository.FixtureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoricalLeagueTableService {

	private final FixtureRepository fixtureRepository;

	/**
	 * Ricostruisce la classifica usando soltanto le fixture concluse prima della
	 * fixture target.
	 */
	@Transactional(readOnly = true)
	public Map<Long, Integer> calculatePositionsBefore(Fixture targetFixture) {


		List<Fixture> previousFixtures = fixtureRepository.findCompletedLeagueFixturesBefore(
				targetFixture.getLeagueId(), targetFixture.getSeason(), targetFixture.getDate());
		
		Long teams = fixtureRepository.countTeams(targetFixture.getLeagueId(), targetFixture.getSeason());

		int neutralPosition = (teams == null || teams == 0) ? 10 : (teams.intValue() + 1) / 2;

		if (previousFixtures.isEmpty()) {

			Map<Long, Integer> neutralPositions = new HashMap<>();

			neutralPositions.put(targetFixture.getHomeTeamId(), neutralPosition);

			neutralPositions.put(targetFixture.getAwayTeamId(), neutralPosition);

			return neutralPositions;
		}

		Map<Long, TeamStanding> standings = new HashMap<>();

		/*
		 * Inseriamo anche le squadre della fixture target. Questo è importante nelle
		 * prime giornate, quando potrebbero non avere ancora disputato partite.
		 */
		standings.put(targetFixture.getHomeTeamId(), new TeamStanding(targetFixture.getHomeTeamId()));

		standings.put(targetFixture.getAwayTeamId(), new TeamStanding(targetFixture.getAwayTeamId()));

		for (Fixture fixture : previousFixtures) {
			TeamStanding home = standings.computeIfAbsent(fixture.getHomeTeamId(), TeamStanding::new);

			TeamStanding away = standings.computeIfAbsent(fixture.getAwayTeamId(), TeamStanding::new);

			int homeGoals = safeInt(fixture.getHomeGoals());
			int awayGoals = safeInt(fixture.getAwayGoals());

			home.addGoals(homeGoals, awayGoals);
			away.addGoals(awayGoals, homeGoals);

			if (homeGoals > awayGoals) {
				home.addPoints(3);
			} else if (homeGoals < awayGoals) {
				away.addPoints(3);
			} else {
				home.addPoints(1);
				away.addPoints(1);
			}
		}

		List<TeamStanding> ordered = new ArrayList<>(standings.values());

		ordered.sort(Comparator.comparingInt(TeamStanding::getPoints).reversed()
				.thenComparing(Comparator.comparingInt(TeamStanding::getGoalDifference).reversed())
				.thenComparing(Comparator.comparingInt(TeamStanding::getGoalsScored).reversed())
				.thenComparingLong(TeamStanding::getTeamId));

		Map<Long, Integer> positions = new LinkedHashMap<>();

		for (int index = 0; index < ordered.size(); index++) {
			positions.put(ordered.get(index).getTeamId(), index + 1);
		}

		return positions;
	}

	private int safeInt(Integer value) {
		return value == null ? 0 : value;
	}

	private static class TeamStanding {

		private final Long teamId;

		private int points;
		private int goalsScored;
		private int goalsConceded;

		private TeamStanding(Long teamId) {
			this.teamId = teamId;
		}

		private void addPoints(int value) {
			this.points += value;
		}

		private void addGoals(int scored, int conceded) {
			this.goalsScored += scored;
			this.goalsConceded += conceded;
		}

		private Long getTeamId() {
			return teamId;
		}

		private int getPoints() {
			return points;
		}

		private int getGoalsScored() {
			return goalsScored;
		}

		private int getGoalDifference() {
			return goalsScored - goalsConceded;
		}
	}
}