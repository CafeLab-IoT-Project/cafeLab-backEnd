package com.cafemetrix.cafelab.monitoring.interfaces.rest.transform;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.TelemetryRecordResource;

public class TelemetryRecordResourceFromEntityAssembler {
    public static TelemetryRecordResource toResourceFromEntity(TelemetryRecord entity) {
        return new TelemetryRecordResource(
                entity.getId(),
                entity.getCoffeeLotId(),
                entity.getTemperature(),
                entity.getHumidity(),
                entity.getTimestamp()
        );
    }
}