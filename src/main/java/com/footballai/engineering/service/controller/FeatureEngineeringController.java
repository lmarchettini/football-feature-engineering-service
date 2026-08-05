package com.footballai.engineering.service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.footballai.engineering.service.dto.FeatureEngineeringRunRequest;
import com.footballai.engineering.service.dto.FeatureEngineeringRunResponse;
import com.footballai.engineering.service.service.ManualFeatureEngineeringService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(
        "/api/v1/feature-engineering"
)
@RequiredArgsConstructor
@Validated
public class FeatureEngineeringController {

    private final ManualFeatureEngineeringService
            manualFeatureEngineeringService;

    @PostMapping("/run")
    public ResponseEntity<FeatureEngineeringRunResponse>
            run(
                    @Valid
                    @RequestBody
                    FeatureEngineeringRunRequest request
            ) {

        return ResponseEntity.ok(
                manualFeatureEngineeringService
                        .run(request)
        );
    }
}