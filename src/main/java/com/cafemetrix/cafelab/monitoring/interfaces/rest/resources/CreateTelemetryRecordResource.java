package com.cafemetrix.cafelab.monitoring.interfaces.rest.resources;
import java.time.LocalDateTime;

public record CreateTelemetryRecordResource(Long coffeeLotId, Double temperature, Double humidity, LocalDateTime timestamp) {}