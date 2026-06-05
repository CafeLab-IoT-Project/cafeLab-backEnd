package com.cafemetrix.cafelab.monitoring.domain.model.aggregates;

import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateEnvironmentThresholdCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.valueobjects.HumidityThreshold;
import com.cafemetrix.cafelab.monitoring.domain.model.valueobjects.TemperatureThreshold;
import com.cafemetrix.cafelab.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EnvironmentThreshold extends AuditableAbstractAggregateRoot<EnvironmentThreshold> {
    @Column(nullable = false, unique = true)
    private Long coffeeLotId;

    @Embedded
    private TemperatureThreshold  temperatureThreshold;

    @Embedded
    private HumidityThreshold humidityThreshold;

    public EnvironmentThreshold(CreateEnvironmentThresholdCommand command) {
        this.coffeeLotId = command.coffeeLotId();
        this.temperatureThreshold = new TemperatureThreshold(command.minTemperature(), command.maxTemperature());
        this.humidityThreshold = new HumidityThreshold(command.minHumidity(), command.maxHumidity());
    }

    public void updateThresholds(Double minTemp, Double maxTemp, Double minHum, Double maxHum) {
        this.temperatureThreshold = new TemperatureThreshold(minTemp, maxTemp);
        this.humidityThreshold = new HumidityThreshold(minHum, maxHum);
    }
}
