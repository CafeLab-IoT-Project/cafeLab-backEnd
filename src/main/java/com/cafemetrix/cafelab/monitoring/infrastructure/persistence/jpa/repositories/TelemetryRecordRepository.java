package com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TelemetryRecordRepository extends JpaRepository<TelemetryRecord, Long> {
    List<TelemetryRecord> findByCoffeeLotIdOrderByTimestampAsc(Long coffeeLotId);
    boolean existsByCoffeeLotId(Long coffeeLotId);

}