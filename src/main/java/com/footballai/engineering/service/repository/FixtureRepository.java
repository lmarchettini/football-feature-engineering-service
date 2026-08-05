package com.footballai.engineering.service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.footballai.engineering.service.entity.Fixture;

public interface FixtureRepository extends JpaRepository<Fixture, Long> {
	
	
	@Query("""
		    SELECT f
		    FROM Fixture f
		    WHERE f.date >= :fromDate
		      AND f.date < :toDate
		      AND f.status NOT IN ('FT', 'AET', 'PEN', 'AWD', 'CANC')
		    ORDER BY f.date ASC
		""")
		List<Fixture> findUpcomingFixtures(
		        @Param("fromDate") LocalDateTime fromDate,
		        @Param("toDate") LocalDateTime toDate,
		        Pageable pageable
		);
	
	@Query("""
		    SELECT f
		    FROM Fixture f
		    WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
		      AND f.homeGoals IS NOT NULL
		      AND f.awayGoals IS NOT NULL
		      AND EXISTS (
		          SELECT 1
		          FROM PredictionFeature pf
		          WHERE pf.fixtureId = f.id
		            AND pf.isTrainable = false
		      )
		    ORDER BY f.date ASC
		""")
		List<Fixture> findSettledFixturesWithUpcomingFeatures(
		        Pageable pageable
		);

	@Query("""
		    SELECT f
		    FROM Fixture f
		    WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
		      AND f.homeGoals IS NOT NULL
		      AND f.awayGoals IS NOT NULL
		      AND NOT EXISTS (
		          SELECT 1
		          FROM PredictionFeature pf
		          WHERE pf.fixtureId = f.id
		      )
		    ORDER BY f.date ASC
		""")
		List<Fixture> findFixturesWithoutFeatures(Pageable pageable);
	
	
	@Query("""
		    SELECT f
		    FROM Fixture f
		    WHERE f.date >= :fromDate
		      AND f.date < :toDate
		      AND f.status NOT IN ('FT', 'AET', 'PEN', 'AWD', 'CANC')
		      AND NOT EXISTS (
		          SELECT 1
		          FROM PredictionFeature pf
		          WHERE pf.fixtureId = f.id
		      )
		    ORDER BY f.date ASC
		""")
		List<Fixture> findUpcomingFixturesWithoutFeatures(
		        @Param("fromDate") LocalDateTime fromDate,
		        @Param("toDate") LocalDateTime toDate,
		        Pageable pageable
		);

    @Query("""
        SELECT f
        FROM Fixture f
        WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
          AND f.date < :beforeDate
          AND (f.homeTeamId = :teamId OR f.awayTeamId = :teamId)
        ORDER BY f.date DESC
    """)
    List<Fixture> findPreviousMatches(
            Long teamId,
            LocalDateTime beforeDate,
            Pageable pageable
    );

    @Query("""
        SELECT f
        FROM Fixture f
        WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
          AND f.date < :beforeDate
          AND (
              (f.homeTeamId = :homeTeamId AND f.awayTeamId = :awayTeamId)
              OR
              (f.homeTeamId = :awayTeamId AND f.awayTeamId = :homeTeamId)
          )
        ORDER BY f.date DESC
    """)
    List<Fixture> findPreviousHeadToHead(
            Long homeTeamId,
            Long awayTeamId,
            LocalDateTime beforeDate,
            Pageable pageable
    );
    
    @Query("""
    	    SELECT f
    	    FROM Fixture f
    	    WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
    	      AND f.date < :beforeDate
    	      AND f.homeTeamId = :teamId
    	    ORDER BY f.date DESC
    	""")
    	List<Fixture> findPreviousHomeMatches(
    	        Long teamId,
    	        LocalDateTime beforeDate,
    	        Pageable pageable
    	);

    	@Query("""
    	    SELECT f
    	    FROM Fixture f
    	    WHERE f.status IN ('FT', 'AET', 'PEN', 'AWD')
    	      AND f.date < :beforeDate
    	      AND f.awayTeamId = :teamId
    	    ORDER BY f.date DESC
    	""")
    	List<Fixture> findPreviousAwayMatches(
    	        Long teamId,
    	        LocalDateTime beforeDate,
    	        Pageable pageable
    	);
    	
    	@Query("""
    		    SELECT f
    		    FROM Fixture f
    		    WHERE f.leagueId = :leagueId
    		      AND f.season = :season
    		      AND f.status IN ('FT', 'AET', 'PEN', 'AWD')
    		      AND f.homeGoals IS NOT NULL
    		      AND f.awayGoals IS NOT NULL
    		      AND f.date < :beforeDate
    		    ORDER BY f.date ASC, f.id ASC
    		""")
    		List<Fixture> findCompletedLeagueFixturesBefore(
    		        Long leagueId,
    		        Integer season,
    		        LocalDateTime beforeDate
    		);
    	
    	@Query(
    		    value = """
    		        SELECT COUNT(*)
    		        FROM (
    		            SELECT home_team_id AS team_id
    		            FROM fixtures
    		            WHERE league_id = :leagueId
    		              AND season = :season
    		              AND home_team_id IS NOT NULL

    		            UNION

    		            SELECT away_team_id AS team_id
    		            FROM fixtures
    		            WHERE league_id = :leagueId
    		              AND season = :season
    		              AND away_team_id IS NOT NULL
    		        ) AS league_teams
    		        """,
    		    nativeQuery = true
    		)
    		Long countTeams(
    		        @Param("leagueId") Long leagueId,
    		        @Param("season") Integer season
    		);
    	
 
    	@Query("""
    	        SELECT f
    	        FROM Fixture f
    	        WHERE f.date >= :fromDate
    	          AND f.date < :toDate
    	          AND f.status NOT IN ('FT', 'AET', 'PEN', 'AWD', 'CANC')
    	          AND EXISTS (
    	              SELECT 1
    	              FROM PredictionFeature pf
    	              WHERE pf.fixtureId = f.id
    	          )
    	        ORDER BY f.date ASC, f.id ASC
    	        """)
    	List<Fixture> findLiveFixturesWithFeatures(
    	        @Param("fromDate") LocalDateTime fromDate,
    	        @Param("toDate") LocalDateTime toDate
    	);
    	
    	@Query("""
    	        SELECT f
    	        FROM Fixture f
    	        WHERE f.date >= :fromDate
    	          AND f.date < :toDate
    	          AND f.status IN ('FT', 'AET', 'PEN', 'AWD')
    	          AND f.homeGoals IS NOT NULL
    	          AND f.awayGoals IS NOT NULL
    	          AND EXISTS (
    	              SELECT 1
    	              FROM PredictionFeature pf
    	              WHERE pf.fixtureId = f.id
    	          )
    	        ORDER BY f.date ASC, f.id ASC
    	        """)
    	List<Fixture> findHistoricalFixturesWithFeatures(
    	        @Param("fromDate") LocalDateTime fromDate,
    	        @Param("toDate") LocalDateTime toDate
    	);
}