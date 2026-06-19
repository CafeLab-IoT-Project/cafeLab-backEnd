package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.MonitoringAlert;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.MonitoringAlertRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/monitoring-alerts")
@Tag(name = "Monitoring Alerts", description = "Endpoints para la gestión de alertas ambientales IoT")
public class MonitoringAlertsController {

    private final MonitoringAlertRepository alertRepository;

    public MonitoringAlertsController(MonitoringAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping("/coffee-lot/{lotId}")
    public ResponseEntity<List<MonitoringAlert>> getAlertsByLot(@PathVariable Long lotId) {
        List<MonitoringAlert> alerts = alertRepository.findAllByCoffeeLotIdOrderByIdDesc(lotId);
        return ResponseEntity.ok(alerts);
    }

    @PatchMapping("/{alertId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long alertId) {
        return alertRepository.findById(alertId).map(alert -> {
            alert.markAsRead();
            alertRepository.save(alert);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}