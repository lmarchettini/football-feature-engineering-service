package com.footballai.engineering.service.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.footballai.engineering.service.entity.FixtureStatistic;

public interface FixtureStatisticRepository
        extends JpaRepository<FixtureStatistic, Long> {

    Optional<FixtureStatistic> findByFixtureIdAndTeamId(
            Long fixtureId,
            Long teamId
    );
    
    @Query("""
    	    SELECT fs
    	    FROM FixtureStatistic fs
    	    WHERE fs.fixtureId IN :fixtureIds
    	""")
    	List<FixtureStatistic> findByFixtureIdIn(
    	        @Param("fixtureIds") Collection<Long> fixtureIds
    	);
}