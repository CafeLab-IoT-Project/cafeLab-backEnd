package com.cafemetrix.cafelab.monitoring.interfaces.rest.transform;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.EnvironmentThresholdResource;

public class EnvironmentThresholdResourceFromEntityAssembler {
    public static EnvironmentThresholdResource toResourceFromEntity(EnvironmentThreshold entity) {
        return new EnvironmentThresholdResource(
                entity.getId(),
                entity.getCoffeeLotId(),
                entity.getTemperatureThreshold().minTemperature(),
                entity.getTemperatureThreshold().maxTemperature(),
                entity.getHumidityThreshold().minHumidity(),
                entity.getHumidityThreshold().maxHumidity()
        );
    }
}