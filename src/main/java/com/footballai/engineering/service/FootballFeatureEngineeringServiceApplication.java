package com.footballai.engineering.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FootballFeatureEngineeringServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballFeatureEngineeringServiceApplication.class, args);
	}

}
