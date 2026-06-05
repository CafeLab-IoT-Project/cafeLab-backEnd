package com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.EnvironmentThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EnvironmentThresholdRepository extends JpaRepository<EnvironmentThreshold, Long> {
    Optional<EnvironmentThreshold> findByCoffeeLotId(Long coffeeLotId);
    boolean existsByCoffeeLotId(Long coffeeLotId);
}