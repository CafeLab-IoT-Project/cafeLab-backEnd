package com.cafemetrix.cafelab.monitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record TemperatureThreshold(Double minTemperature, Double maxTemperature) {

    public TemperatureThreshold {
        if (minTemperature == null || maxTemperature == null) {
            throw new IllegalArgumentException("Temperature thresholds cannot be null");
        }
        if (minTemperature > maxTemperature) {
            throw new IllegalArgumentException("Minimum temperature cannot be greater than maximum temperature");
        }
    }
}