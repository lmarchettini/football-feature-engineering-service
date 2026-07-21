package com.footballai.engineering.service.repository;

import com.footballai.engineering.service.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}