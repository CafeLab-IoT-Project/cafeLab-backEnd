package com.cafemetrix.cafelab.monitoring.domain.model.commands;

public record UpdateEnvironmentThresholdCommand(
        Long coffeeLotId,
        Double minTemperature,
        Double maxTemperature,
        Double minHumidity,
        Double maxHumidity
) {
    public UpdateEnvironmentThresholdCommand {
        if (coffeeLotId == null) {
            throw new IllegalArgumentException("Coffee lot ID cannot be null");
        }
        if (minTemperature == null || maxTemperature == null) {
            throw new IllegalArgumentException("Temperature settings cannot be null");
        }
        if (minHumidity == null || maxHumidity == null) {
            throw new IllegalArgumentException("Humidity settings cannot be null");
        }
    }
}