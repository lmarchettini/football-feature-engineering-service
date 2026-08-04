package com.footballai.engineering.service.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footballai.engineering.service.entity.GoalProbability;
import com.footballai.engineering.service.entity.GoalProbabilityId;

public interface GoalProbabilityRepository
        extends JpaRepository<
            GoalProbability,
            GoalProbabilityId
        > {

    List<GoalProbability>
    findByFixtureIdInAndEngine(
            Collection<Long> fixtureIds,
            String engine
    );
}