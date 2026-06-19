package com.cafemetrix.cafelab.monitoring.application.internal.commandservices;

import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.TelemetryRecord;
import com.cafemetrix.cafelab.monitoring.domain.model.aggregates.MonitoringAlert;
import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateTelemetryRecordCommand;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordCommandService;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.TelemetryRecordRepository;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.EnvironmentThresholdRepository;
import com.cafemetrix.cafelab.monitoring.infrastructure.persistence.jpa.repositories.MonitoringAlertRepository;
import org.springframework.stereotype.Service;

@Service
public class TelemetryRecordCommandServiceImpl implements TelemetryRecordCommandService {

    private final TelemetryRecordRepository telemetryRecordRepository;
    private final EnvironmentThresholdRepository thresholdRepository;
    private final MonitoringAlertRepository alertRepository;

    public TelemetryRecordCommandServiceImpl(TelemetryRecordRepository telemetryRecordRepository,
                                             EnvironmentThresholdRepository thresholdRepository,
                                             MonitoringAlertRepository alertRepository) {
        this.telemetryRecordRepository = telemetryRecordRepository;
        this.thresholdRepository = thresholdRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public Long handle(CreateTelemetryRecordCommand command) {
        TelemetryRecord telemetryRecord = new TelemetryRecord(command);
        telemetryRecordRepository.save(telemetryRecord);

        thresholdRepository.findByCoffeeLotId(command.coffeeLotId()).ifPresent(threshold -> {
            if (command.temperature() > threshold.getTemperatureThreshold().maxTemperature()) {
                alertRepository.save(new MonitoringAlert(command.coffeeLotId(), "TEMPERATURE", command.temperature(), threshold.getTemperatureThreshold().maxTemperature()));
            }
            if (command.temperature() < threshold.getTemperatureThreshold().minTemperature()) {
                alertRepository.save(new MonitoringAlert(command.coffeeLotId(), "TEMPERATURE", command.temperature(), threshold.getTemperatureThreshold().minTemperature()));
            }
            if (command.humidity() > threshold.getHumidityThreshold().maxHumidity()) {
                alertRepository.save(new MonitoringAlert(command.coffeeLotId(), "HUMIDITY", command.humidity(), threshold.getHumidityThreshold().maxHumidity()));
            }
            if (command.humidity() < threshold.getHumidityThreshold().minHumidity()) {
                alertRepository.save(new MonitoringAlert(command.coffeeLotId(), "HUMIDITY", command.humidity(), threshold.getHumidityThreshold().minHumidity()));
            }
        });

        return telemetryRecord.getId();
    }
}