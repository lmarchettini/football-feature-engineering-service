package com.footballai.engineering.service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.footballai.engineering.service.dto.UpcomingFixtureFeaturesResponse;
import com.footballai.engineering.service.service.UpcomingFixtureFeaturesService;
import com.footballai.engineering.service.utils.FeatureRetrievalMode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
public class UpcomingFixtureFeaturesController {

    private final UpcomingFixtureFeaturesService upcomingFixtureFeaturesService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingFixtureFeaturesResponse>>
    findUpcomingFixtures(

            @RequestParam("from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @RequestParam("to")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @RequestParam("featureVersion")
            String featureVersion,

            @RequestParam(
                    value = "mode",
                    defaultValue = "LIVE"
            )
            FeatureRetrievalMode mode
    ) {

        List<UpcomingFixtureFeaturesResponse> response =
                upcomingFixtureFeaturesService.findFixtures(
                        from,
                        to,
                        featureVersion,
                        mode
                );

        return ResponseEntity.ok(response);
    }
}