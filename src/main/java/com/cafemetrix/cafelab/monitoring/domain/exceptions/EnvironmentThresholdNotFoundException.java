package com.cafemetrix.cafelab.monitoring.domain.exceptions;

public class EnvironmentThresholdNotFoundException extends RuntimeException {
    public EnvironmentThresholdNotFoundException(Long coffeeLotId) {
        super("Configuración de umbrales ambientales no encontrada para el lote con ID: " + coffeeLotId);
    }
}