package com.footballai.engineering.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/api/**").allowedOrigins("http://localhost:5173").allowedMethods("GET", "POST", "OPTIONS");

		/*
		 * Manteniamo accessibile anche l'endpoint già usato dal Ranking.
		 */
		registry.addMapping("/features/**").allowedOrigins("http://localhost:5173", "http://localhost:8087")
				.allowedMethods("GET", "OPTIONS");
	}
}