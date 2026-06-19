package com.cafemetrix.cafelab.monitoring.domain.model.aggregates;

import com.cafemetrix.cafelab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "monitoring_alerts")
public class MonitoringAlert extends AuditableAbstractAggregateRoot<MonitoringAlert> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long coffeeLotId;

    @Column(nullable = false)
    private String metricType;

    @Column(nullable = false)
    private Double currentValue;

    @Column(nullable = false)
    private Double thresholdViolated;

    @Column(nullable = false)
    private boolean isRead = false;

    public MonitoringAlert() {}

    public MonitoringAlert(Long coffeeLotId, String metricType, Double currentValue, Double thresholdViolated) {
        this.coffeeLotId = coffeeLotId;
        this.metricType = metricType;
        this.currentValue = currentValue;
        this.thresholdViolated = thresholdViolated;
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}