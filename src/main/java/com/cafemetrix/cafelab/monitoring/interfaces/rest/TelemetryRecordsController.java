package com.cafemetrix.cafelab.monitoring.interfaces.rest;

import com.cafemetrix.cafelab.monitoring.domain.model.commands.CreateTelemetryRecordCommand;
import com.cafemetrix.cafelab.monitoring.domain.model.queries.GetTelemetryRecordsByCoffeeLotIdQuery;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordCommandService;
import com.cafemetrix.cafelab.monitoring.domain.services.TelemetryRecordQueryService;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.CreateTelemetryRecordResource;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.resources.TelemetryRecordResource;
import com.cafemetrix.cafelab.monitoring.interfaces.rest.transform.TelemetryRecordResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/telemetry-records", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Telemetry Records", description = "Endpoints for real-time IoT sensor telemetry capturing and analysis")
public class TelemetryRecordsController {

    private final TelemetryRecordCommandService telemetryCommandService;
    private final TelemetryRecordQueryService telemetryQueryService;

    public TelemetryRecordsController(TelemetryRecordCommandService telemetryCommandService, TelemetryRecordQueryService telemetryQueryService) {
        this.telemetryCommandService = telemetryCommandService;
        this.telemetryQueryService = telemetryQueryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> recordTelemetry(@RequestBody CreateTelemetryRecordResource resource) {
        var command = new CreateTelemetryRecordCommand(resource.coffeeLotId(), resource.temperature(), resource.humidity(), resource.timestamp());
        Long recordId = telemetryCommandService.handle(command);
        if (recordId == null || recordId <= 0) {
            throw new IllegalStateException("Fallo en la ingesta del paquete de datos.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", recordId, "status", "SUCCESS"));
    }

    @GetMapping("/coffee-lot/{coffeeLotId}")
    public ResponseEntity<List<TelemetryRecordResource>> getTelemetryHistory(@PathVariable Long coffeeLotId) {
        var query = new GetTelemetryRecordsByCoffeeLotIdQuery(coffeeLotId);
        var history = telemetryQueryService.handle(query);

        var resources = history.stream()
                .map(TelemetryRecordResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }
}