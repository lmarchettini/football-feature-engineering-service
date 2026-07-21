package com.footballai.engineering.service.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footballai.engineering.service.entity.PredictionFeature;

public interface PredictionFeatureRepository extends JpaRepository<PredictionFeature, Long> {
	
	List<PredictionFeature> findByFixtureIdIn(
            Collection<Long> fixtureIds
    );
	
	List<PredictionFeature>
    findByFixtureIdInAndFeatureVersion(
            Collection<Long> fixtureIds,
            String featureVersion
    );
}