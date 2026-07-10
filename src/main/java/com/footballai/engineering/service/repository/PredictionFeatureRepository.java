package com.footballai.engineering.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.footballai.engineering.service.entity.PredictionFeature;

public interface PredictionFeatureRepository extends JpaRepository<PredictionFeature, Long> {
}