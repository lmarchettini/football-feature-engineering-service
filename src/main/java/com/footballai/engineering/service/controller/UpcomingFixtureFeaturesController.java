package com.footballai.engineering.service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.footballai.engineering.service.dto.UpcomingFeatureResponse;
import com.footballai.engineering.service.service.UpcomingFixtureFeaturesService;
import com.footballai.engineering.service.utils.FeatureRetrievalMode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/features")
@RequiredArgsConstructor
public class UpcomingFixtureFeaturesController {

    private final UpcomingFixtureFeaturesService
            upcomingFixtureFeaturesService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<UpcomingFeatureResponse>>
            findUpcomingFixtures(

            @RequestParam("from")
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime from,

            @RequestParam("to")
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME
            )
            LocalDateTime to,

            @RequestParam(
                    value = "mode",
                    defaultValue = "LIVE"
            )
            FeatureRetrievalMode mode
    ) {
        List<UpcomingFeatureResponse> response =
                upcomingFixtureFeaturesService
                        .findFixtures(
                                from,
                                to,
                                mode
                        );

        return ResponseEntity.ok(response);
    }
}