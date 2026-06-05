package com.cafemetrix.cafelab.monitoring.interfaces.rest.resources;

public record EnvironmentThresholdResource(Long id, Long coffeeLotId, Double minTemperature, Double maxTemperature, Double minHumidity, Double maxHumidity) {}