package com.footballai.engineering.service.entity;

import java.io.Serializable;
import java.util.Objects;

public class GoalProbabilityId implements Serializable {

    private Long fixtureId;
    private String engine;

    public GoalProbabilityId() {
    }

    public GoalProbabilityId(
            Long fixtureId,
            String engine
    ) {
        this.fixtureId = fixtureId;
        this.engine = engine;
    }

    public Long getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(Long fixtureId) {
        this.fixtureId = fixtureId;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof GoalProbabilityId other)) {
            return false;
        }

        return Objects.equals(
                fixtureId,
                other.fixtureId
        ) && Objects.equals(
                engine,
                other.engine
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                fixtureId,
                engine
        );
    }
}