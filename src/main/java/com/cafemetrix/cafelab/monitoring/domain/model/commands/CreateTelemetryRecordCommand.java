package com.cafemetrix.cafelab.monitoring.domain.model.commands;

import java.time.LocalDateTime;

public record CreateTelemetryRecordCommand(
        Long coffeeLotId,
        Double temperature,
        Double humidity,
        LocalDateTime timestamp
) {
    public CreateTelemetryRecordCommand {
        if (coffeeLotId == null) {
            throw new IllegalArgumentException("Coffee lot ID cannot be null");
        }
        if (temperature == null) {
            throw new IllegalArgumentException("Temperature reading cannot be null");
        }
        if (humidity == null) {
            throw new IllegalArgumentException("Humidity reading cannot be null");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
    }
}