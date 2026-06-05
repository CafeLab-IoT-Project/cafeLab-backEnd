package com.cafemetrix.cafelab.monitoring.domain.model.queries;

public record GetEnvironmentThresholdByCoffeeLotIdQuery(Long coffeeLotId) {
    public GetEnvironmentThresholdByCoffeeLotIdQuery {
        if (coffeeLotId == null) throw new IllegalArgumentException("Coffee Lot ID cannot be null");
    }
}