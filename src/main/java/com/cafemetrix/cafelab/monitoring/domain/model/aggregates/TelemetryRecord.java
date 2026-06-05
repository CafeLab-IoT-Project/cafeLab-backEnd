package com.cafemetrix.cafelab.monitoring.domain.model.aggregates;

import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateTelemetryRecordCommand;
import com.cafemetrix.cafelab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class TelemetryRecord extends AuditableAbstractAggregateRoot<TelemetryRecord> {

    @Column(nullable = false)
    private Long coffeeLotId;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public TelemetryRecord(CreateTelemetryRecordCommand command) {
        this.coffeeLotId = command.coffeeLotId();
        this.temperature = command.temperature();
        this.humidity = command.humidity();
        this.timestamp = command.timestamp();
    }
}