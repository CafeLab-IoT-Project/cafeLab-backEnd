package com.cafemetrix.cafelab.monitoring.domain.model.queries;

public record GetTelemetryRecordsByCoffeeLotIdQuery(Long coffeeLotId) {
    public GetTelemetryRecordsByCoffeeLotIdQuery {
        if (coffeeLotId == null) throw new IllegalArgumentException("Coffee Lot ID cannot be null");
    }
}