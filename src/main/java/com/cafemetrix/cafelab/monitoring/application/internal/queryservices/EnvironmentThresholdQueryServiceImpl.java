package com.cafemetrix.cafelab.monitoring.application.internal.queryservices;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetEnvironmentThresholdByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.EnvironmentThresholdQueryService;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.EnvironmentThresholdRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EnvironmentThresholdQueryServiceImpl implements EnvironmentThresholdQueryService {
    private final EnvironmentThresholdRepository thresholdRepository;

    public EnvironmentThresholdQueryServiceImpl(EnvironmentThresholdRepository thresholdRepository) {
        this.thresholdRepository = thresholdRepository;
    }

    @Override
    public Optional<EnvironmentThreshold> handle(GetEnvironmentThresholdByCoffeeLotIdQuery query) {
        return thresholdRepository.findByCoffeeLotId(query.coffeeLotId());
    }
}