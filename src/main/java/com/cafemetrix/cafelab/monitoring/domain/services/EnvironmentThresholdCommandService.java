package com.cafemetrix.cafelab.monitoring.domain.services;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.UpdateEnvironmentThresholdCommand;

import java.util.Optional;

public interface EnvironmentThresholdCommandService {
    Optional<EnvironmentThreshold> handle(CreateEnvironmentThresholdCommand command);
    Optional<EnvironmentThreshold> handle(UpdateEnvironmentThresholdCommand command);
}