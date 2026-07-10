package com.footballai.engineering.service.repository;

import com.footballai.engineering.service.entity.FixtureStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FixtureStatisticRepository
        extends JpaRepository<FixtureStatistic, Long> {

    Optional<FixtureStatistic> findByFixtureIdAndTeamId(
            Long fixtureId,
            Long teamId
    );
}