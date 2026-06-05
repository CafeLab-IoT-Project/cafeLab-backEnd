package com.cafemetrix.cafelab.monitoring.domain.exceptions;

public class TelemetryRecordNotFoundException extends RuntimeException {
    public TelemetryRecordNotFoundException(Long telemetryRecordId) {
        super("Registro de telemetría IoT con ID " + telemetryRecordId + " no encontrado.");
    }
}