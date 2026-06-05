package com.cafemetrix.cafelab.monitoring.domain.services;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetTelemetryRecordsByCoffeeLotIdQuery;
import java.util.List;

public interface TelemetryRecordQueryService {
    List<TelemetryRecord> handle(GetTelemetryRecordsByCoffeeLotIdQuery query);
}