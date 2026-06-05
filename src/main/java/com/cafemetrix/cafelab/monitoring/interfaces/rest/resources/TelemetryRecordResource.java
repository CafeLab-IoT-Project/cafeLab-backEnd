package com.cafemetrix.cafelab.monitoring.interfaces.rest.resources;
import java.time.LocalDateTime;

public record TelemetryRecordResource(Long id, Long coffeeLotId, Double temperature, Double humidity, LocalDateTime timestamp) {}