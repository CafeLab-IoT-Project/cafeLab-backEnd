package com.cafemetrix.cafelab.monitoring.application.internal.commandservices;

import com.cafemetrix.cafelab.monitoring.application.internal.outboundservices.acl.ExternalProductionService;
import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.UpdateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdCommandService;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.EnvironmentThresholdRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnvironmentThresholdCommandServiceImpl implements EnvironmentThresholdCommandService {

    private final EnvironmentThresholdRepository thresholdRepository;
    private final ExternalProductionService externalProductionService;

    public EnvironmentThresholdCommandServiceImpl(EnvironmentThresholdRepository thresholdRepository, ExternalProductionService externalProductionService) {
        this.thresholdRepository = thresholdRepository;
        this.externalProductionService = externalProductionService;
    }

    @Override
    public Optional<EnvironmentThreshold> handle(CreateEnvironmentThresholdCommand command) {
        if (!externalProductionService.existsCoffeeLot(command.coffeeLotId())) {
            throw new IllegalArgumentException("Cannot create threshold. Coffee Lot with ID " + command.coffeeLotId() + " does not exist.");
        }

        if (thresholdRepository.existsByCoffeeLotId(command.coffeeLotId())) {
            throw new IllegalArgumentException("Threshold configuration already exists for Coffee Lot ID " + command.coffeeLotId());
        }

        var environmentThreshold = new EnvironmentThreshold(command);
        thresholdRepository.save(environmentThreshold);
        return Optional.of(environmentThreshold);
    }

    @Override
    public Optional<EnvironmentThreshold> handle(UpdateEnvironmentThresholdCommand command) {
        var existingThreshold = thresholdRepository.findByCoffeeLotId(command.coffeeLotId())
                .orElseThrow(() -> new IllegalArgumentException("Threshold configuration not found for Coffee Lot ID " + command.coffeeLotId()));

        existingThreshold.updateThresholds(
                command.minTemperature(),
                command.maxTemperature(),
                command.minHumidity(),
                command.maxHumidity()
        );

        thresholdRepository.save(existingThreshold);
        return Optional.of(existingThreshold);
    }
}