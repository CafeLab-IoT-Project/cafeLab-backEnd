package com.cafemetrix.cafelab.monitoring.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record HumidityThreshold(Double minHumidity, Double maxHumidity) {

    public HumidityThreshold {
        if (minHumidity == null || maxHumidity == null) {
            throw new IllegalArgumentException("Humidity thresholds cannot be null");
        }
        if (minHumidity < 0.0 || maxHumidity > 100.0) {
            throw new IllegalArgumentException("Humidity must be a percentage between 0 and 100");
        }
        if (minHumidity > maxHumidity) {
            throw new IllegalArgumentException("Minimum humidity cannot be greater than maximum humidity");
        }
    }
}