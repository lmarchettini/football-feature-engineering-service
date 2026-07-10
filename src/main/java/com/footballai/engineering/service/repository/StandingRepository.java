package com.footballai.engineering.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footballai.engineering.service.entity.Standing;

public interface StandingRepository extends JpaRepository<Standing, Long> {

    Optional<Standing> findByLeagueIdAndSeasonAndTeamId(
            Long leagueId,
            Integer season,
            Long teamId
    );
}