package com.cafemetrix.cafelab.monitoring.domain.services;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetEnvironmentThresholdByCoffeeLotIdQuery;
import java.util.Optional;

public interface EnvironmentThresholdQueryService {
    Optional<EnvironmentThreshold> handle(GetEnvironmentThresholdByCoffeeLotIdQuery query);
}