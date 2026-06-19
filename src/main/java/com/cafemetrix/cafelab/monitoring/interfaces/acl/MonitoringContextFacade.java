package com.cafemetrix.cafelab.monitoring.interfaces.acl;

import java.util.List;

public interface MonitoringContextFacade {
    Long createEnvironmentThreshold(
            Long coffeeLotId,
            Double minTemperature,
            Double maxTemperature,
            Double minHumidity,
            Double maxHumidity,
            Integer syncIntervalSeconds
    );
}
