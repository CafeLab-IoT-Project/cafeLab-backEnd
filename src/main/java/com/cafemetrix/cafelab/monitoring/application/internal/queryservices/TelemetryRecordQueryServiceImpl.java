package com.cafemetrix.cafelab.monitoring.application.internal.queryservices;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetTelemetryRecordsByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordQueryService;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TelemetryRecordQueryServiceImpl implements TelemetryRecordQueryService {
    private final TelemetryRecordRepository telemetryRepository;

    public TelemetryRecordQueryServiceImpl(TelemetryRecordRepository telemetryRepository) {
        this.telemetryRepository = telemetryRepository;
    }

    @Override
    public List<TelemetryRecord> handle(GetTelemetryRecordsByCoffeeLotIdQuery query) {
        return telemetryRepository.findByCoffeeLotIdOrderByTimestampAsc(query.coffeeLotId());
    }
}