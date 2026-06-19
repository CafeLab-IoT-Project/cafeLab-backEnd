package com.cafemetrix.cafelab.production.application.internal.outboundservices.acl;

import com.cafemetrix.cafelab.monitoring.interfaces.acl.MonitoringContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExternalMonitoringService {
    private final MonitoringContextFacade monitoringContextFacade;

    public ExternalMonitoringService(MonitoringContextFacade profilesContextFacade) {
        this.monitoringContextFacade = profilesContextFacade;
    }

    public Optional<Long> createEnvironmentThreshold(
            Long coffeeLotId,
            Double minTemperature,
            Double maxTemperature,
            Double minHumidity,
            Double maxHumidity,
            Integer syncIntervalSeconds
    ) {
        var environmentThreshold = monitoringContextFacade.createEnvironmentThreshold(
                coffeeLotId,
                minTemperature,
                maxTemperature,
                minHumidity,
                maxHumidity,
                syncIntervalSeconds
        );
        return environmentThreshold == 0L ? Optional.empty() : Optional.of(environmentThreshold);
    }
}
