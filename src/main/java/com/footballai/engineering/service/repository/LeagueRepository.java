package com.footballai.engineering.service.repository;

import com.footballai.engineering.service.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}