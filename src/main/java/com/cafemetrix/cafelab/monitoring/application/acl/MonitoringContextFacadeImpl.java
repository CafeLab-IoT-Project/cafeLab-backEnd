package com.cafemetrix.cafelab.monitoring.application.acl;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdCommandService;
import com.cafemetrix.cafelab.monitoring.interfaces.acl.MonitoringContextFacade;
import org.springframework.stereotype.Service;

@Service
public class MonitoringContextFacadeImpl implements MonitoringContextFacade {
    private final EnvironmentThresholdCommandService environmentThresholdCommandService;

    public MonitoringContextFacadeImpl(EnvironmentThresholdCommandService environmentThresholdCommandService) {
        this.environmentThresholdCommandService = environmentThresholdCommandService;
    }

    @Override
    public Long createEnvironmentThreshold(Long coffeeLotId, Double minTemperature, Double maxTemperature, Double minHumidity, Double maxHumidity, Integer syncIntervalSeconds) {
        var createEnvironmentThresholdCommand = new CreateEnvironmentThresholdCommand(
                coffeeLotId,
                minTemperature,
                maxTemperature,
                minHumidity,
                maxHumidity,
                syncIntervalSeconds
        );
        var environmentThreshold = environmentThresholdCommandService.handle(createEnvironmentThresholdCommand);
        return environmentThreshold.map(EnvironmentThreshold::getId).orElse(0L);
    }
}
