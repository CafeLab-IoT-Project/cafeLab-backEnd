package com.cafemetrix.cafelab.monitoring.interfaces.rest.resources;

public record CreateEnvironmentThresholdResource(Long coffeeLotId, Double minTemperature, Double maxTemperature, Double minHumidity, Double maxHumidity) {}