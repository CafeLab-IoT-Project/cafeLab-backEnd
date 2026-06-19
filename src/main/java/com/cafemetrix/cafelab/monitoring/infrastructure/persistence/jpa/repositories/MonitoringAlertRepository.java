package com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.MonitoringAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonitoringAlertRepository extends JpaRepository<MonitoringAlert, Long> {
    List<MonitoringAlert> findAllByCoffeeLotIdOrderByIdDesc(Long coffeeLotId);
}