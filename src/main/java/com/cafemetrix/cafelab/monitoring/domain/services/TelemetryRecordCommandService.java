package com.cafemetrix.cafelab.monitoring.domain.services;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateTelemetryRecordCommand;

import java.util.Optional;

public interface TelemetryRecordCommandService {
    Optional<TelemetryRecord> handle(CreateTelemetryRecordCommand command);
}